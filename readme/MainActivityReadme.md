# MainActivity

## Layout

![image](https://github.com/advanced-8-int-p/PINTube/assets/116724657/0f9e47a9-c592-44ba-93f4-2b2c1b5e2282)

기본적인 프래그먼트를 띄울 프래그먼트 뷰 하나와, 상세정보를 표시할 프래그먼트뷰,

그리고 바텀 네비게이션을 커스텀해서 구성했다.

바텀 네비게이션은 모션 레이아웃을 사용해서 특정 조건일때 다른 액션을 취할 수 있도록 프로팅 버튼이 추가되는 형태로 구현했다.

![image](https://velog.velcdn.com/images/guysang/post/3202af84-b71c-4da6-9dbd-d85503a3c065/image.gif)

디테일 페이지를 들어가게 되면 버튼을 눌러 공유하기와, 북마크하기를 선택 할 수있고

평소엔 쇼츠 페이지로 넘어가게 된다.

## [MainActivity.kt](https://github.com/advanced-8-int-p/PINTube/blob/dev/app/src/main/java/com/example/pintube/ui/main/MainActivity.kt)

매인 액티비티에서 다루는건 , 바텀네비게이션과 디테일 페이지 관리다

```kotlin
  private fun initBottomNav() = with(binding) {
        navView.setupWithNavController(navController)
        navView.background = null

        mainFabShare.setOnClickListener {
            val currentFragmentInstance =
                supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment_activity_main)?.childFragmentManager?.fragments?.first() as VideoDataInterface

            val videoUrl = currentFragmentInstance.getVideoUrl()
            ShareLink(this@MainActivity, videoUrl)
            mainMotion.transitionToStart()
        }

        mainFabPin.setOnClickListener {
            val currentFragmentInstance =
                supportFragmentManager
                    .findFragmentById(R.id.nav_host_fragment_activity_main)?.childFragmentManager?.fragments?.first() as VideoDataInterface

            val videoId = currentFragmentInstance.getVideoId()
            viewModel.onClickBookmark(videoId)
            currentFragmentInstance.initData()
            mainMotion.transitionToStart()
        }
    }
```
바텀 네비게이션의 액션을 관리하는데 네비컨트롤러를 연결하고

숨겨진 버튼의 클릭리스너를 정의한다, 숨겨진 버튼은 디테일 페이지를 들어가게되면 볼 수 있는데

각각 디테일 페이지의 내용 공유, 북마크 저장 기능을 담당한다.

```kotlin
 private fun detailState() = with(sharedViewModel) {
        lifecycleScope.launch {
            viewState.collect {
                detail = it
                if (it) {
                    with(binding) {
                        mainFab.setOnClickListener {
                            if (mainMotion.currentState == mainMotion.startState) {
                                mainMotion.transitionToEnd()
                            } else {
                                mainMotion.transitionToStart()
                            }
                        }
                        mainFab.setImageResource(R.drawable.ic_main_fab_plus)
                        mainFab.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    mainFab.context,
                                    R.color.main_color
                                )
                            )
                    }
                } else {
                    with(binding.mainFab) {
                        setImageResource(R.drawable.ic_nav_fab_shorts)
                        backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    context,
                                    R.color.white
                                )
                            )
                        setOnClickListener {
                            startActivity(
                                Intent(
                                    this@MainActivity,
                                    ShortsActivity::class.java
                                )
                            )
                        }
                    }
                }
            }
        }
    }
```
메인 fab버튼의 상태를 정의하는데, 디테일 페이지를 켰을때를 공유 뷰모델에서 관찰해서

있으면 아이콘이 바뀌면서 버튼을 클릭했을때 공유하기, 북마크하기 버튼이 나타나게 되고,

없으면 쇼츠 액티비티로 이동하게 된다.



### SharedViewModel
```kotlin
class MainSharedViewModel: ViewModel() {
    private val _viewState = MutableStateFlow(false)
    val viewState: StateFlow<Boolean> = _viewState.asStateFlow()

    fun updateViewState(boolean: Boolean) {
        _viewState.value = boolean
    }
}
```

공유 액티비티를 이용해서 현재 디테일페이지를 열었는지 안 열었는지 상태를 

stateFlow를 이용해서 구별하게만들었다.
# SplashScreen

## Layout

![](https://velog.velcdn.com/images/guysang/post/6845d809-ec48-43b0-8d81-c444e62b63f8/image.gif)

모션 레이아웃을 이용해서 구현했다.

트랜지션을 4개를 이용해서 애니메이션을 부분별로 구별해 키프레임별로 상태를 주지 않고 자연스럽게 이어지도록 구현했다

![image](https://github.com/advanced-8-int-p/PINTube/assets/116724657/979c8f37-81a0-435d-b82e-a129bb8e61ec)

![image](https://github.com/advanced-8-int-p/PINTube/assets/116724657/750a67af-0b0f-4d2d-a3ef-efac783af943)

## Splash.kt

```kotlin
private fun setMotion() = with(binding.mlSplashLayout) {
        post {
            setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionChange(
                    motionLayout: MotionLayout,
                    startId: Int,
                    endId: Int,
                    progress: Float
                ) {
                    when(endId) {
                        R.id.loading -> {
                            if (progress > 0.9f) {
                                navigateToMain()
                                motionLayout.setTransitionListener(null)
                            }
                        }
                    }
                }

                override fun onTransitionStarted(
                    motionLayout: MotionLayout,
                    startId: Int,
                    endId: Int
                ) {
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float
                ) {
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                    when (currentId) {
                        R.id.end -> setTransitionAction(R.id.end, R.id.statusbar, 450)
                        R.id.statusbar -> setTransitionAction(R.id.statusbar, R.id.loading, 350)
                        R.id.loading -> navigateToMain()
                    }
                }
            })
            setTransitionAction(R.id.start, R.id.end, 350)
        }
    }
```
트랜지션 리스너를 사용해서 애니메이션이 끝날때마다 트랜지션을 전환해가면서 자연스럽게 이어지도록했다.

마지막엔 Main액티비티를 실행 시키고 꺼지는 로직을 구현했다.

.post를 붙여 뷰의 레이아웃이 완료되고 나서 작업이 실행되게 했다.
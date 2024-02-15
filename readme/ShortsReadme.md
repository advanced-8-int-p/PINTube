# Shorts

## Layout

![image](https://github.com/advanced-8-int-p/PINTube/assets/116724657/c79a7ddb-73b9-4e05-9ba8-07f2f12f2ddd)

액티비티는 뷰페이저로 구성했고 안의 아이템은 유튜브 플레이어와,

댓글, 북마크, 공유 버튼으로 구성했다.

![image](https://github.com/advanced-8-int-p/PINTube/assets/116724657/0a068c0e-ca60-47ba-b6bf-e6f27c0e071c)

댓글을 누르면 바텀싯 뷰로 댓글창이 나오게 했다.


## ShortsActivity

```kotlin
    private fun setButton() = with(binding){
        ivShortsBack.setOnClickListener {
            finish()
        }

        ivShortsSearch.setOnClickListener { 
            startActivity(Intent(this@ShortsActivity,SearchActivity::class.java))
        }
    }
```
상단 버튼의 구현부

액티비티를 종료하거나 검색 액티비티를 실행시킨다

```kotlin
    private fun initViewModel() {
        viewModel.getShortsVideos()

        viewModel.videos.observe(this) {
            adapter.submitList(it)
        }

        viewModel.comments.observe(this) {
            if (it.isEmpty().not()) {
                commentAdapter.submitList(it)
            } else {
                commentAdapter.submitList(listOf(CommentsItem.NoComments))
            }
        }
    }
```
뷰모델에선 댓글과, 영상 데이터를 관찰하고 어댑터에 넣어준다.

```kotlin
    private fun setPlayer() = with(binding) {
        vpShortsViewpager.adapter = adapter
        vpShortsViewpager.orientation = ViewPager2.ORIENTATION_VERTICAL
        vpShortsViewpager.offscreenPageLimit = 10
    }
```
쇼츠가 이미 나왔던게 다시 나오는 현상이 발생했다.

비디오 플레이어를 초기화를 수동으로 해봤지만

에러가 나거나, 플레이어가 동작할 수 없다는 경고 표시가 플레이어 내에서 뜨는둥 해결하지못해 미리 보기 데이터를 늘려 재사용하는 데이터를 줄이는 조치를 취했다.

```kotlin
    private fun setCommentSheet() = with(binding){
        bottomSheetDialog.setContentView(commentSheetView)
        commentRecyclerView.adapter = commentAdapter
        commentRecyclerView.layoutManager = LinearLayoutManager(this@ShortsActivity)
    }

        private fun setCommentSheet(count: String) = with(commentSheetView) {
        findViewById<TextView>(R.id.tv_comment_count).text = count
        findViewById<ImageView>(R.id.iv_comment_close).setOnClickListener {
            bottomSheetDialog.hide()
        }
    }

        private fun onCommentChecked(item: ShortsItem.Item) {
        item.id?.let { viewModel.getComments(it) }
        item.commentCount?.let { setCommentSheet(it) }
        bottomSheetDialog.show()
    }
```
댓글창 은 바텀 싯 다이얼로그를 이용해서 구현했다.

count가 들어간부분은 댓글 옆 댓글수를 나타내는부분과, 댓글창 닫기버튼을 나타낸다.

onCommentChecked는 댓글 창을 여는 버튼부분인데, 눌렀을때 뷰모델에서 댓글리스트를 불러오게 된다.


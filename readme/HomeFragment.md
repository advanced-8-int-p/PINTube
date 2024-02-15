# HomeFragment

# [ 1 ] 홈 화면 구성

## (1) Layout : [fragment_home.xml](https://)
- **탑바** - 앱 로고와 이름, 검색버튼을 표시하는 탑바
- **리사이클러뷰** - 하위 멀티뷰를 적용하여 인기영상, 카테고리 등을 담는 전체적인 리사이클러뷰.\
  카테고리 검색 결과를 표시하기 위해 span 2 짜리 그리드 레이아웃으로 되어있다. 
- 다이얼로그는 카테고리를 동적으로 추가/삭제하기 위해 레이어를 겹쳐 만들었다.
  - **카테고리 편집 다이얼로그**
  - **카테고리 추가 다이얼로그**

## [home_item_popular.xml](https://)
인기영상을 띄워주는 홈 어댑터의 아이템. 헤더와 뷰페이저로 구성되어있다. 뷰페이저는 횡방향으로 구성했다.

## [popular_item.xml]()
인기영상 뷰페이저에 들어가는 비디오 정보 아이템.\
비디오 썸네일을 바탕으로, 비디오 제목과 채널 이름, 채널 썸네일 등이 표시된다.\
저장을 위한 핀 아이콘 버튼도 표시된다.

## [home_item_category.xml]()
홈 어댑터에 들어가는 카테고리 아이템.\
카테고리 헤더와 편집 버튼, 카테고리 리사이클러뷰로 구성된다.

## [video_item.xml]()
홈 어댑터에 들어가는 카테고리 검색 결과 비디오 정보 아이템.\
종 스크롤로 구성하여, 하위 리사이클러뷰가 아닌 홈 어댑터에 그대로 들어간다.

## (2) Fragment : [HomeFragment.kt](https://)

## (3) ViewModel : [HomeViewModel.kt](https://)

## () Adapter : [HomeAdapter.kt](https://)

## () Adapter : [PopularVideoAdapter.kt](https://)

## () Adapter : [CategoryAdapter.kt](https://)

## () Adapter : [CategoryEditDialogAdapter.kt](https://)

# [ 2 ] 주요 구현 기능

## 멀티뷰타입 리사이클러뷰
(스크롤 예시 영상)
홈 화면에서 탑바를 제외하고 전체적으로 스크롤 되도록 하는데, 인기 영상, 카테고리 검색 결과도
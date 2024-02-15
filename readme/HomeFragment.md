# HomeFragment

# \[ 1 \] 홈 화면 구성 파일

## (1) Layout

### [fragment_home.xml](https://)

- **탑바** - 앱 로고와 이름, 검색버튼을 표시하는 탑바
- **리사이클러뷰** - 하위 멀티뷰를 적용하여 인기영상, 카테고리 등을 담는 전체적인 리사이클러뷰.\
  카테고리 검색 결과를 표시하기 위해 span 2 짜리 그리드 레이아웃으로 되어있다.
- 다이얼로그는 카테고리를 동적으로 추가/삭제하기 위해 레이어를 겹쳐 만들었다.
    - **카테고리 편집 다이얼로그**
    - **카테고리 추가 다이얼로그**

### [home_item_popular.xml](https://)

인기영상을 띄워주는 홈 어댑터의 아이템. 헤더와 뷰페이저로 구성되어있다. 뷰페이저는 횡방향으로 구성했다.

### [popular_item.xml]()

인기영상 뷰페이저에 들어가는 비디오 정보 아이템.\
비디오 썸네일을 바탕으로, 비디오 제목과 채널 이름, 채널 썸네일 등이 표시된다.\
저장을 위한 핀 아이콘 버튼도 표시된다.

### [home_item_category.xml]()

홈 어댑터에 들어가는 카테고리 아이템.\
카테고리 헤더와 편집 버튼, 카테고리 리사이클러뷰로 구성된다.

### [video_item.xml]()

홈 어댑터에 들어가는 카테고리 검색 결과 비디오 정보 아이템.\
종 스크롤로 구성하여, 하위 리사이클러뷰가 아닌 홈 어댑터에 그대로 들어간다.

## (2) SourceCode

홈프래그먼트 관련하여 구현한 코드 파일들은 home 패키지에 모아두었다.

[com.example.pintube.ui.home](https://github.com/advanced-8-int-p/PINTube/tree/main/app/src/main/java/com/example/pintube/ui/home)

[//]: # (## &#40;2&#41; Fragment : [HomeFragment.kt]&#40;https://&#41;)

[//]: # ()

[//]: # (## &#40;3&#41; ViewModel : [HomeViewModel.kt]&#40;https://&#41;)

[//]: # ()

[//]: # (## &#40;&#41; Adapter : [HomeAdapter.kt]&#40;https://&#41;)

[//]: # ()

[//]: # (## &#40;&#41; Adapter : [PopularVideoAdapter.kt]&#40;https://&#41;)

[//]: # ()

[//]: # (## &#40;&#41; Adapter : [CategoryAdapter.kt]&#40;https://&#41;)

[//]: # ()

[//]: # (## &#40;&#41; Adapter : [CategoryEditDialogAdapter.kt]&#40;https://&#41;)

# \[ 2 \] 주요 구현 기능

## 멀티뷰타입 리사이클러뷰

(스크롤 예시 영상)
홈 화면에서 탑바를 제외하고 전체적으로 스크롤 되도록 하는데, 인기 영상, 카테고리 헤더, 카테고리 검색 결과도 모두 함께 스크롤되어야 한다.\
그런데 카테고리 검색 결과에 무한스크롤까지 적용이 되므로 스크롤뷰를 사용하면 성능이 나빠지기 때문에 리사이클러뷰를 사용해야 하는데,
각기 다른 아이템들을 리사이클러뷰로 구성하려면 멀티뷰타입으로 구현해야 했다.\
멀티 뷰타입을 구현하기 위해서 뷰타입과 함께 각 아이템에 쓰일 데이터 등을 묶어 클래스로 만들고, sealed interface로 묶어 관리했다.

### [SealedMulti.kt]()

<details>
<summary>SealedMulti.kt 코드 펼치기/접기</summary>

```kotlin
const val MULTI_POPULAR = 1
const val MULTI_CATEGORY = 2
const val MULTI_VIDEO = 3
const val MULTI_HEADER = 0
const val MULTI_LOADING = -1

sealed interface SealedMulti {

    val viewType: Int

    data object Header : SealedMulti {
        override val viewType: Int
            get() = MULTI_HEADER
    }

    data class Popular(
        val videoAdapter: PopularVideoAdapter
    ) : SealedMulti {
        override val viewType: Int = MULTI_POPULAR
    }

    data class Category(
        val categoryAdapter: CategoryAdapter,
    ) : SealedMulti {
        override val viewType: Int = MULTI_CATEGORY
    }

    data class Video(
        val videoItemData: VideoItemData,
    ) : SealedMulti {
        override val viewType: Int = MULTI_VIDEO
    }

    data object Loading : SealedMulti {
        override val viewType: Int
            get() = MULTI_LOADING
    }

}
```

</details>

실드 인터페이스에 뷰타입 변수를 두고 하위 클래스들에서 뷰타입 값을 오버라이드 해두어서,
어댑터에서 getItemViewType을 할 때 `sealedMultis[position].viewType`이라고만 넘겨주면 된다.

```kotlin
// 멀티뷰타입 리사이클러뷰 어댑터 getItemViewType 함수
override fun getItemViewType(position: Int): Int = sealedMultis[position].viewType
```

## 리사이클러뷰 안의 리사이클러뷰

인기영상, 카테고리는 홈 어댑터 안에 횡스크롤로 중첩하여 들어가는 리사이클러뷰이므로
리사이클러뷰의 어댑터에서 또다시 어댑터를 넣어주어야 하는 구조다.\
이 내부 어댑터는 실드 인터페이스 하위 데이터 클래스의 파라미터로 만들어서,
중첩된 어댑터들까지도 프래그먼트에서 모두 만들어서 집어넣을 수 있도록 했다.

(코드)

## 저장한 카테고리명으로 검색

(카테고리 횡방향 스크롤 예시영상)

저장된 카테고리는 횡방향 리사이클러뷰로 표시된다. 이 카테고리 이름을 클릭하면 해당 카테고리명으로
검색을 수행하여 결과를 아래에 종방향 무한스크롤로 표시하게 된다.

레트로핏을 담당한 팀원이 `GetSearchVideosUseCase`를 만들어두어서, `Hilt`를 사용해 뷰모델에 주입받고,
invoke로 작성해두어서 함수처럼 사용하여 괄호 열고 검색어를 집어넣으면 쿼리 결과를 리스트로 반환해준다.\
`GetSearchVideosUseCase`는 내부적으로 일단 검색어로 쿼리한 결과에 대해서 video id로 다시 쿼리를 보내서 재생 시간 등의 비디오 정보를 받아오고,
channel id로 또 쿼리를 보내서 채널 썸네일 등의 정보를 받아온 뒤, 비디오 정보와 채널 썸네일을 묶어서
하나의 클래스에 담아 리스트로 반환해준다.\
그러면 이를 받아서 내가 사용할 데이터 클래스로 변환하여 저장하고, 카테고리 검색 결과를 띄울 때
비디오 정보들을 표시해주면 된다.

## 카테고리 편집/추가

(카테고리 편집에서 스크롤, 삭제, 추가, 모두 삭제하고 빈 리스트 영상)

카테고리 목록을 확인, 삭제, 추가하는 기능은 간단하게 다이얼로그로 구현하려고 했다.\
하지만 다이얼로그에 현재 카테고리 목록을 스크롤이 되도록 표시하고, 각 카테고리 옆에 삭제 버튼을 눌러
동적으로 삭제되는 것이 반영되도록 만들려면 다이얼로그로는 불가능했다.\
그렇다고 겨우 이 기능을 수행하고자 새로 프래그먼트나 액티비티를 띄우기는 이상했다.

그래서 홈 프래그먼트에 레이아웃을 겹쳐서 다이얼로그처럼 기능하도록 구현했다.
카테고리 편집 버튼을 누르면 visibility를 조정하여 다이얼로그 레이어가 보이도록 하고, 카테고리
목록을 반영하여 표시 및 동적 추가, 삭제를 가능하도록 했다.

(홈 프래그먼트 레이아웃 컴포넌트 트리 visibility 켜고 끄는 예시 영상)

## 카테고리 저장 및 불러오기

카테고리 목록은 추가/삭제한대로 저장되어, 다음에 앱을 실행해도 저장된대로 불러와져야 한다.

카테고리 리스트만 저장하면 되므로, 간단하게 json으로 변환하여 SharedPreferences로 저장하고 불러오게 구현했다.\
변환에는 Gson을 사용했다.

## 카테고리 검색 결과 무한스크롤

유튜브 API의 검색 결과는 한 번에 최대 50개씩 받아오게 되어있다. 카테고리 검색결과는 종스크롤로
표시했고, 최하단까지 스크롤이 되면 이를 인식하여 다음 페이지를 검색하여 추가적으로 표시하도록
무한 스크롤을 구현했다. 각 검색 시 다음 검색에 대한 토큰이 함께 오는데, 이를 SharedPreferences로
저장하고 다음 검색에 사용한다. key 값으로 페이지 넘버를 함께 붙여 저장한다.

## 영상 클릭 시 상세 페이지로 넘겨주기

인기영상이나 카테고리 검색 결과로 표시된 영상 정보를 클릭하면 상세페이지로 연결된다.

홈 프래그먼트와 상세페이지 프레그먼트는 바텀내비게이션에 종속되어있으므로 이에 맞춰서 페이지를 넘겨야 한다.\
메인 액티비티에 상세페이지로 넘기는 메서드가 구현되어있어서, 프래그먼트에서 액티비티를 가져와서 MainActivity로
캐스팅하여 해당 메서드를 호출해주면 된다.

```kotlin
item.id?.let { (activity as MainActivity).initDetailFragment(it) }
```

[//]: # ()

[//]: # ()

[//]: # ()

[//]: # ()

[//]: # ()

[//]: # ()

[//]: # ()

[//]: # ()

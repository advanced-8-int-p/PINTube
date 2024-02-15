# Data 관리

## Sever

### Retrofit
<details>
<summary> retrofit.kt</summary>

```kotlin
object YouTubeApi {
    private const val YOUTUBE_BASE_URL = "https://www.googleapis.com/youtube/v3/"

    private fun createOkHttpClient() : OkHttpClient {
        val connectionPool = ConnectionPool(5, 5, TimeUnit.MINUTES)

        val interceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        }
        else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        val keyInterceptor = Interceptor { chain ->
            var request = chain.request()
            val url = request.url.newBuilder()
                .addQueryParameter("key", BuildConfig.YOUTUBE_API_KEY)
                .build()
            request = request.newBuilder()
                .url(url)
                .build()
            chain.proceed(request)
        }

        return OkHttpClient.Builder()
            .connectionPool(connectionPool)
            .addInterceptor(keyInterceptor)
            .addNetworkInterceptor(interceptor)
            .build()
    }

    private val youtubeRetrofit =
        Retrofit.Builder().baseUrl(YOUTUBE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                createOkHttpClient()
            ).build()

    val youtubeNetwork: YoutubeSearchService = youtubeRetrofit.create(YoutubeSearchService::class.java)
}
```

</details>

<details>
<summary> retrofitInterface.kt </summary>

```kotlin
interface YoutubeSearchService {

    @GET("search")
    suspend fun searchResult (
        @Query("forContentOwner") forContentOwner: String? = null, // 콘텐츠 소유자 동영상 검색 아래는 내 동영상
        @Query("forMine") forMine: String? = null, // 위랑 아래랑 둘중하나 혹은 0
        @Query("channelId") channelId: String? = null, //
        @Query("eventType") eventType: String? = null, // completed - 완료된 브로드캐스트, live , upcoming - 예정된 방송
        @Query("maxResults") maxResults: Int? = 50,
        @Query("part") part: String = "snippet",
        @Query("order") order: String = "relevance", //date – 리소스를 만든 날짜를 기준, rating – 높은 평가부터 낮은 평가순, relevance – 검색어와의 관련성을 기준, title – 제목알파벳순, viewCount – 리소스가 조회수가 높은 순
        @Query("regionCode") regionCode: String = "kr", // 국가에서 볼 수 있는 동영상
        @Query("pageToken") pageToken: String? = null,
        @Query("relevanceLanguage") relevanceLanguage: String = "ko", // 언어랑 관련성 높은 영상
        @Query("q") query: String = "react", // 검색어
        @Query("videoDuration") videoDuration: String = "any", // long – 20분보다 긴 동영상, medium – 4분에서 20분 사이의 동영상, short – 4분 미만의 동영상
        @Query("type") type: String = "video", // channel, playlist, video
        @Query("videoType") videoType: String = "any", // any – 모든 동영상을 반환합니다, episode - 프로그램의 에피소드만 검색합니다., movie - 영화만 검색합니다
        @Query("videoSyndicated") videoSyndicated: String = "true", // any – 배급 여부에 관계 없이 모든 동영상을 반환합니다.,true – 배급된 동영상만 검색합니다. 외부에서 재생할 수 있는 동영상
    ) : GenericApiResponse<SearchItemResponse>

    @GET("videos")
    suspend fun getPopularVideo (
        @Query("part") part: String = "snippet,contentDetails,statistics,player,liveStreamingDetails,topicDetails",
        @Query("chart") chart: String = "mostPopular",
        @Query("h1") h1: String = "ko",
        @Query("maxResults") maxResult: Int = 50,
        @Query("regionCode") regionCode: String = "kr",
    ) : GenericApiResponse<ItemResponse>

    @GET("videos")
    suspend fun getContentDetails (
        @Query("part") part: String = "snippet,contentDetails,statistics,player,liveStreamingDetails,topicDetails",
        @Query("id") ids: List<String>,
        @Query("h1") h1: String = "ko",
        @Query("maxResults") maxResult: Int? = 50,
        @Query("regionCode") regionCode: String = "kr",
    ) : GenericApiResponse<ItemResponse>

    @GET("channels")
    suspend fun getChannelDetails(
        @Query("part") part: String = "snippet,statistics,brandingSettings,contentDetails,localizations,topicDetails",
        @Query("id") id: List<String>,
        @Query("maxResults") maxResult: Int? = 50,
    ) : GenericApiResponse<ItemResponse>


    @GET("commentThreads")
    suspend fun getComments (
        @Query("part") part: String = "snippet,id,replies",
        @Query("videoId") videoId: String,
        @Query("maxResults") maxResult: Int? = 100,
        @Query("order") order: String? = "relevance"
    ) : GenericApiResponse<ItemResponse>
}
```
</details>

레트로핏에선 검색결과, 인기비디오, 채널 정보, 댓글 정보들을 볼 수 있는데

할당량을 최소화 하기 위해서 전부 결과값을 최대치로 받아오게 했고

아이디, 채널등 특정 아이디에 대한 정보가 필요한 부분도 리스트로 값을 넣어서 할당량을 최소화 하게 만들었다.

#### [DTO (Response)](https://github.com/advanced-8-int-p/PINTube/tree/dev/app/src/main/java/com/example/pintube/data/remote/dto)

[DTO](https://github.com/advanced-8-int-p/PINTube/tree/dev/app/src/main/java/com/example/pintube/data/remote/dto) 는 유튜브 데이터를 전부 가져오게 했고, search와 다른 리스폰들의 id 값이 타입이 달라 둘을 나눠준뒤 제너릭 클래스로 선언했다.

```kotlin
data class GenericApiResponse<T>(
    val etag: String?,
    val items: List<T>?,
    val kind: String?,
    val regionCode: String?,
    val nextPageToken: String?,
    val prevPageToken: String?,
    val pageInfo: PageInfo?
)
```

#### Repository

ApiRepository.kt

```kotlin
interface ApiRepository {
    suspend fun getPopularVideo(): List<VideoEntity>?
    suspend fun getContentDetails(idList: List<String>): List<VideoEntity>?
    suspend fun getChannelDetails(idList: List<String>): List<ChannelEntity>?
    suspend fun getComments(videoId: String): List<CommentEntity?>?
    suspend fun getRandomShorts(): List<SearchEntity>?
    fun getNextPageToken(): String?
    suspend fun searchResult(query: String, pageToken: String? = null): List<SearchEntity>?
}
```

<details>
<summary> ApiRepositoryImpl.kt </summary>

```kotlin
package com.example.pintube.data.repository.sever

import com.example.pintube.data.remote.retrofit.YoutubeSearchService
import com.example.pintube.data.remote.dto.ItemResponse
import com.example.pintube.data.remote.dto.SearchItemResponse
import com.example.pintube.domain.entitiy.ChannelEntity
import com.example.pintube.domain.entitiy.CommentEntity
import com.example.pintube.domain.entitiy.SearchEntity
import com.example.pintube.domain.entitiy.VideoEntity
import com.example.pintube.domain.repository.ApiRepository
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(
    private val remoteDataSource: YoutubeSearchService,
) : ApiRepository {

    private var nextPageToken: String? = null
    override suspend fun searchResult(
        query: String,
        pageToken: String?,
    ): List<SearchEntity>? = remoteDataSource
        .searchResult(
            query = query,
            pageToken = pageToken
        ).let {
            nextPageToken = it.nextPageToken
            it.items?.map { item ->
                convertSearchEntity(item)
            }
        }

    override suspend fun getRandomShorts(): List<SearchEntity>? = remoteDataSource
        .searchResult(query = "#shorts #쇼츠", videoDuration = "short").let {
            nextPageToken = it.nextPageToken
            it.items?.map { item ->
                convertSearchEntity(item)
            }
        }

    override suspend fun getPopularVideo()
            : List<VideoEntity>? = remoteDataSource.getPopularVideo().let {
        nextPageToken = it.nextPageToken
        it.items?.map { item ->
            convertVideoEntity(item)
        }
    }

    override suspend fun getContentDetails(
        idList: List<String>
    ): List<VideoEntity>? = remoteDataSource.getContentDetails(ids = idList).items?.map { item ->
        convertVideoEntity(item)
    }

    override suspend fun getChannelDetails(
        idList: List<String>
    ): List<ChannelEntity>? = remoteDataSource.getChannelDetails(id = idList).items?.map { item ->
        convertChannelEntity(item)
    }

    override suspend fun getComments(
        videoId: String
    ): List<CommentEntity?>? = remoteDataSource.getComments(videoId = videoId).let {
        nextPageToken = it.nextPageToken
        it.items?.map { item ->
            convertCommentRepliesEntity(item)
        }
    }

    override fun getNextPageToken(): String? = nextPageToken

    private fun convertSearchEntity(item: SearchItemResponse): SearchEntity {
        return SearchEntity(
            id = item.id?.videoId ?: "",
            publishedAt = item.snippet?.publishedAt ?: "",
            channelId = item.snippet?.channelId ?: "",
            title = item.snippet?.title ?: "",
            description = item.snippet?.description ?: "",
            localizedTitle = item.snippet?.localized?.title ?: "",
            localizedDescription = item.snippet?.localized?.description ?: "",
            thumbnailHigh = item.snippet?.thumbnails?.high?.url ?: "",
            thumbnailMedium = item.snippet?.thumbnails?.medium?.url ?: "",
            thumbnailLow = item.snippet?.thumbnails?.default?.url ?: "",
            channelTitle = item.snippet?.channelTitle ?: "",
            liveBroadcastContent = item.snippet?.liveBroadcastContent ?: ""
        )
    }

    private fun convertVideoEntity(item: ItemResponse): VideoEntity {
        return VideoEntity(
            id = item.id ?: "",
            publishedAt = item.snippet?.publishedAt ?: "",
            channelId = item.snippet?.channelId ?: "",
            title = item.snippet?.title ?: "",
            description = item.snippet?.description ?: "",
            thumbnailHigh = item.snippet?.thumbnails?.high?.url ?: "",
            thumbnailMedium = item.snippet?.thumbnails?.medium?.url ?: "",
            thumbnailLow = item.snippet?.thumbnails?.default?.url ?: "",
            channelTitle = item.snippet?.channelTitle ?: "",
            tags = item.snippet?.tags?.toList(),
            categoryId = item.snippet?.categoryId ?: "",
            liveBroadcastContent = item.snippet?.liveBroadcastContent ?: "",
            defaultLanguage = item.snippet?.defaultLanguage ?: "",
            localizedTitle = item.snippet?.localized?.title ?: "",
            localizedDescription = item.snippet?.localized?.description ?: "",
            defaultAudioLanguage = item.snippet?.defaultAudioLanguage ?: "",
            duration = item.contentDetails?.duration ?: "",
            dimension = item.contentDetails?.dimension ?: "",
            definition = item.contentDetails?.definition ?: "",
            caption = item.contentDetails?.caption ?: "",
            licensedContent = item.contentDetails?.licensedContent,
            projection = item.contentDetails?.projection ?: "",
            viewCount = item.statistics?.viewCount ?: "",
            likeCount = item.statistics?.likeCount ?: "",
            favoriteCount = item.statistics?.favoriteCount ?: "",
            commentCount = item.statistics?.commentCount ?: "",
            player = item.player?.embedHtml ?: "",
            topicDetails = item.topicDetails?.topicCategories?.toList()
        )
    }

    private fun convertChannelEntity(
        item: ItemResponse
    ): ChannelEntity = ChannelEntity(
        id = item.id ?: "",
        title = item.brandingSettings?.channel?.title ?: "",
        description = item.brandingSettings?.channel?.description ?: "",
        customUrl = item.snippet?.customUrl ?: "",
        publishedAt = item.snippet?.publishedAt ?: "",
        thumbnailHigh = item.snippet?.thumbnails?.high?.url ?: "",
        thumbnailMedium = item.snippet?.thumbnails?.medium?.url ?: "",
        thumbnailLow = item.snippet?.thumbnails?.default?.url ?: "",
        defaultLanguage = item.snippet?.defaultLanguage ?: "",
        localizedTitle = item.snippet?.localized?.title ?: "",
        localizedDescription = item.snippet?.localized?.description ?: "",
        country = item.snippet?.country ?: "",
        like = item.contentDetails?.relatedPlaylists?.like ?: "",
        uploads = item.contentDetails?.relatedPlaylists?.uploads ?: "",
        viewCount = item.statistics?.viewCount ?: "",
        subscriberCount = item.statistics?.subscriberCount ?: "",
        videoCount = item.statistics?.videoCount ?: "",
        bannerImageUrl = item.brandingSettings?.image?.bannerExternalUrl ?: ""
    )

    private fun convertCommentRepliesEntity(
        item: ItemResponse
    ): CommentEntity? {
        val topLevelComment = item.snippet?.topLevelComment?.let { convertCommentEntity(it) }

        val replies = item.replies?.comments?.map { replyItem ->
            convertCommentEntity(replyItem).copy(parentId = item.id)
        } ?: emptyList()

        return topLevelComment?.copy(
            replies = replies,
            totalReplyCount = item.snippet.totalReplyCount
        )
    }

    private fun convertCommentEntity(
        item: ItemResponse,
        parentId: String? = null
    ): CommentEntity = CommentEntity(
        id = item.id ?: "",
        channelId = item.snippet?.channelId ?: "",
        videoId = item.snippet?.videoId ?: "",
        textDisplay = item.snippet?.textDisplay ?: "",
        textOriginal = item.snippet?.textOriginal ?: "",
        authorDisplayName = item.snippet?.authorDisplayName ?: "",
        authorProfileImageUrl = item.snippet?.authorProfileImageUrl ?: "",
        authorChannelId = item.snippet?.authorChannelId?.value ?: "",
        authorChannelUrl = item.snippet?.authorChannelUrl ?: "",
        canRate = item.snippet?.canRate ?: false,
        viewerRating = item.snippet?.viewerRating ?: "",
        likeCount = item.snippet?.likeCount ?: 0,
        publishedAt = item.snippet?.publishedAt ?: "",
        updatedAt = item.snippet?.updatedAt ?: "",
        totalReplyCount = item.snippet?.totalReplyCount ?: 0,
        replies = emptyList(),
        parentId = parentId
    )
}
```
</details>

<br>

**구현 기능**

- 서버에서 인기비디오 리스트를 가져오기
- 최대 50개의 아이디리스트를 입력해 비디오 상세 정보 가져오기
- 채널의 상세 정보 가져오기
- 댓글의 상세정보 가져오기
- 랜덤 쇼츠 영상 가져오기
- 다음 페이지 토큰 가져오기
- 검색 결과 가져오기 기능.

### Entity

[Entity](https://github.com/advanced-8-int-p/PINTube/tree/dev/app/src/main/java/com/example/pintube/domain/entitiy) 는 도메인 패키지에 정리 해두었다.

## Local

### Room

#### Database

<details>
<summary> YoutubeDatabase.kt </summary>

```kotlin
@Database(
    entities = [
        FavoriteEntity::class,
        LocalSearchEntity::class,
        LocalVideoEntity::class,
        LocalChannelEntity::class,
        LocalCommentEntity::class,
        FavoriteCategoryCross::class,
        CategoryEntity::class,
        RecentViewsEntity::class,
    ],
    version = 7
)
@TypeConverters(LocalTypeConverters::class)
abstract class YoutubeDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDAO
    abstract fun videoDao(): VideoDAO
    abstract fun channelDao(): ChannelDAO
    abstract fun commentDao(): CommentDAO
    abstract fun favoriteDao(): FavoriteDAO

    abstract fun categoryDao(): CategoryDAO
    abstract fun recentViewDao(): RecentViewsDAO

}
```
</details>

#### [DAO](https://github.com/advanced-8-int-p/PINTube/tree/dev/app/src/main/java/com/example/pintube/data/local/dao) 

db에 캐시 데이터를 저장하고 반환하는데, 저장 시간을 기준으로 일정시간이 지났는지 비교하면서 값을 반환한다.

오래된 데이터는 유즈케이스에서 레트로핏으로 호출해 Room에 데이터를 저장해 데이터를 갱신하게 되도록 했다.

북마크 기능과, 최근 시청 목록 저장 기능이 있는데

각각 비디오의 id를 저장하고 room에서 id값을 호출해서 자세한 비디오 데이터를 불러온다.

#### Entity

[Entity](https://github.com/advanced-8-int-p/PINTube/tree/dev/app/src/main/java/com/example/pintube/data/local/entity) Room에 저장되는 테이블 모델인데 도메인 패키지에 넣으니 서버 엔티티랑 헷갈려서 데이터, 로컬 패키지로 옮겨놨다.

#### Repository

<details>
<summary> LocalChannelRepository.kt </summary>

```kotlin
interface LocalChannelRepository {
    suspend fun saveChannel(item: ChannelEntity)

    suspend fun findChannel(channelId: String): LocalChannelEntity?
}

```

</details>

<details>
<summary> LocalCommentRepository.kt </summary>

```kotlin
interface LocalCommentRepository {
    suspend fun saveComment(item: CommentEntity?)
    suspend fun findComment(videoId: String): List<LocalCommentEntity>?
    suspend fun findReplies(parentId: String): List<LocalCommentEntity>?
}
```

</details>

<details>
<summary> LocalFavoriteRepository.kt </summary>

```kotlin
interface LocalFavoriteRepository {
    suspend fun checkIsBookmark(videoId: String): Boolean
    suspend fun addBookmark(videoId: String, category: String = "기본")
    suspend fun addCategoryToFavorite(videoId: String, category: String)
    suspend fun findCategoryVideos(category: String = "기본"): List<String>?
    suspend fun deleteBookmark(videoId: String)
    suspend fun removeCategoryToFavorite(videoId: String, category: String)
}

```

</details>

<details>
<summary> LocalSearchRepository.kt </summary>

```kotlin
interface LocalSearchRepository {
    suspend fun saveSearchResult(item: SearchEntity, query: String)
    suspend fun findSearchRecord(query: String): List<VideoWithThumbnail>?
}
```

</details>

<details>
<summary> LocalVideoRepository.kt </summary>

```kotlin
interface LocalVideoRepository {

    suspend fun saveVideos(item: VideoEntity, isPopular: Boolean? = false)
    suspend fun findVideoDetail(videoId: String): VideoWithThumbnail?
    suspend fun findPopularVideos(): List<VideoWithThumbnail>?
}

```

</details>

<details>
<summary> RecentViewRepository.kt </summary>

```kotlin
interface RecentViewRepository {
    suspend fun addRecentView(id: String)
    suspend fun findRecentView(): List<RecentViewsEntity>?
}
```

</details>

<br>

구현 기능은 레트로핏과 유사한데, 시간이 지나면 데이터를 내보내지 않고

최근 시청목록, 카테고리별 저장한 영상 검색과 같은 기능이 있으며,

채널 프로필과 영상의 정보를 같이 내보내는 VideoWithThumbnail타입의 결과를 반환하기도 한다.

```kotlin
data class VideoWithThumbnail(
    val video: LocalVideoEntity?,
    val thumbnail: ChannelThumbnail?
)

...
interface ChannelDAO {
    ...

    @Query("SELECT thumbnailHigh, thumbnailMedium, thumbnailLow FROM channel_info WHERE id = :channelId")
    fun getChannelThumbnail(channelId: String?): ChannelThumbnail?
}

data class ChannelThumbnail(
    val thumbnailHigh: String?,
    val thumbnailMedium: String?,
    val thumbnailLow: String?,
)
```

### Preference

프리퍼런스는 페이지 토큰을 저장하는 역할을한다.

```kotlin
class PageTokenPrefRepositoryImpl(context: Context): PageTokenPrefRepository {

    private val pref = context.getSharedPreferences("PageToken", Context.MODE_PRIVATE)
    override fun saveNextPageToken(
        query: String,
        page: String,
        token: String?
    ) {
        pref.edit().putString(query + page,token).apply()
    }

    override fun getPageToken(query: String, page: String) = pref.getString(query + page, null)

}
```

검색어 + 페이지 를 키 값으로 다음 페이지 토큰을 저장하고, 페이지에 맞춰 검색어를 입력하면 토큰을 반환하는 로직을 수행한다.

# DI

<details>
<summary> DatabaseModule.kt </summary>

```kotlin
package com.example.pintube.di

import android.content.Context
import androidx.room.Room
import com.example.pintube.data.local.dao.CategoryDAO
import com.example.pintube.data.local.dao.ChannelDAO
import com.example.pintube.data.local.dao.CommentDAO
import com.example.pintube.data.local.dao.FavoriteDAO
import com.example.pintube.data.local.dao.RecentViewsDAO
import com.example.pintube.data.local.dao.SearchDAO
import com.example.pintube.data.local.dao.VideoDAO
import com.example.pintube.data.local.database.YoutubeDatabase
import com.example.pintube.data.remote.retrofit.YouTubeApi
import com.example.pintube.data.remote.retrofit.YoutubeSearchService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideSearchDao(database: YoutubeDatabase): SearchDAO = database.searchDao()

    @Singleton
    @Provides
    fun provideVideoDao(database: YoutubeDatabase): VideoDAO = database.videoDao()

    @Singleton
    @Provides
    fun provideChannelDao(database: YoutubeDatabase): ChannelDAO = database.channelDao()

    @Singleton
    @Provides
    fun provideCommentDao(database: YoutubeDatabase): CommentDAO = database.commentDao()

    @Singleton
    @Provides
    fun provideFavoriteDao(database: YoutubeDatabase): FavoriteDAO = database.favoriteDao()

    @Singleton
    @Provides
    fun provideCategoryDao(database: YoutubeDatabase): CategoryDAO = database.categoryDao()

    @Singleton
    @Provides
    fun provideRecentViewDao(database: YoutubeDatabase): RecentViewsDAO = database.recentViewDao()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): YoutubeDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            YoutubeDatabase::class.java,
            "youtube-database"
        ).fallbackToDestructiveMigration() // 마이그레이션 전략 설정
            .build()

    @Singleton
    @Provides
    fun provideSearchService(): YoutubeSearchService =
        YouTubeApi.youtubeNetwork
}
```
</details>

<details>
<summary> PreferenceModule.kt </summary>

```kotlin
package com.example.pintube.di

import android.content.Context
import com.example.pintube.data.repository.pref.CategoryPrefRepositoryImpl
import com.example.pintube.data.repository.pref.PageTokenPrefRepositoryImpl
import com.example.pintube.domain.repository.CategoryPrefRepository
import com.example.pintube.domain.repository.PageTokenPrefRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object PreferenceModule {

    @Provides
    fun ProvideCategoryPrefRepository(@ApplicationContext context: Context): CategoryPrefRepository = CategoryPrefRepositoryImpl(context)

    @Provides
    fun ProvidePageTokenPrefRepository(@ApplicationContext context: Context): PageTokenPrefRepository = PageTokenPrefRepositoryImpl(context)
}
```
</details>

<details>
<summary> RepositoryModule.kt </summary>

```kotlin
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindApiRepository(repository: ApiRepositoryImpl): ApiRepository

    @Binds
    abstract fun bindLocalSearchRepository(repository: LocalSearchRepositoryImpl): LocalSearchRepository

    @Binds
    abstract fun bindLocalVideoRepository(repository: LocalVideoRepositoryImpl): LocalVideoRepository

    @Binds
    abstract fun bindLocalChannelRepository(repository: LocalChannelRepositoryImpl): LocalChannelRepository

    @Binds
    abstract fun bindLocalCommentRepository(repository: LocalCommentRepositoryImpl): LocalCommentRepository

    @Binds
    abstract fun bindLocalFavoriteRepository(repository: LocalFavoriteRepositoryImpl): LocalFavoriteRepository

    @Binds
    abstract fun bindRecentViewRepository(repository: RecentViewRepositoryImpl): RecentViewRepository
}
```
</details>

데이터 베이스 부분 Room과 RDS처럼 생성 비용이 큰 것들을 싱글톤 컴퍼넌트에 맞춰서 생성해줬고

레포지토리나, 프리퍼런스는 뷰모델 생명주기에 맞춰서 생성해 줬다.

#Domain

## [Usecase](https://github.com/advanced-8-int-p/PINTube/tree/dev/app/src/main/java/com/example/pintube/domain/usecase)

유즈케이스를 만들어서 Room에서 데이터를 확인하고 데이터가 없을시 레트로핏으로 검색하고 다시 룸에 갱신한후 데이터를 불러오는 과정을 유즈케이스로 만들어 다른 사람들 입장에선

뭐가 들어가면 뭐가 나오는지만 알게해 작업할 수 있도록 구현 했다.

package com.example.pintube.data.remote.api.retrofit

import com.example.pintube.data.remote.dto.ApiResponse
import com.example.pintube.data.remote.dto.SearchApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

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
        @Query("relevanceLanguage") relevanceLanguage: String = "ko", // 언어랑 관련성 높은 영상
        @Query("q") query: String = "react", // 검색어
        @Query("type") type: String = "video", // channel, playlist, video
        @Query("videoType") videoType: String = "any", // any – 모든 동영상을 반환합니다, episode - 프로그램의 에피소드만 검색합니다., movie - 영화만 검색합니다
        @Query("videoSyndicated") videoSyndicated: String = "true", // any – 배급 여부에 관계 없이 모든 동영상을 반환합니다.,true – 배급된 동영상만 검색합니다. 외부에서 재생할 수 있는 동영상
    ) : SearchApiResponse

    @GET("videos")
    suspend fun getPopularVideo (
        @Query("part") part: String = "snippet,contentDetails,statistics,player,liveStreamingDetails,topicDetails",
        @Query("chart") chart: String = "mostPopular",
        @Query("maxResults") maxResult: Int = 50,
    ) : ApiResponse

    @GET("videos")
    suspend fun getContentDetails (
        @Query("part") part: String = "contentDetails,statistics,player,liveStreamingDetails,topicDetails",
        @Query("id") ids: List<String>,
        @Query("maxResults") maxResult: Int? = 50,
    ) : ApiResponse

    @GET("channels")
    suspend fun getChannelDetails(
        @Query("part") part: String = "snippet,statistics,brandingSettings,contentDetails,localizations,topicDetails",
        @Query("id") id: List<String>,
        @Query("maxResults") maxResult: Int? = 50,
    ) : ApiResponse


    @GET("commentThreads")
    suspend fun getComments (
        @Query("part") part: String = "snippet",
        @Query("videoId") videoId: String,
        @Query("maxResults") maxResult: Int? = 100,
    ) : ApiResponse
}

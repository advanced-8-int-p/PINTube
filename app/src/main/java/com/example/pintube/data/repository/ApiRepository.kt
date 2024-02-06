package com.example.pintube.data.repository

import com.example.pintube.data.model.VideoModel
import com.example.pintube.data.model.dto.SearchDTO
import com.example.pintube.data.retrofit.YouTubeApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ApiRepository {
    suspend fun searchYoutube(query: String) : MutableList<VideoModel>
    suspend fun getPopularVideo() : MutableList<VideoModel>
}

class ApiRepositoryImpl : ApiRepository {
    private var searchList: MutableList<VideoModel> = mutableListOf()
    private var popularList: MutableList<VideoModel> = mutableListOf()

    private suspend fun searchResult (
        query: String
    ) : SearchDTO {
        return YouTubeApi.youtubeNetwork.searchResult(
            query = query
        )
    }


    override suspend fun searchYoutube(
        query: String
    ): MutableList<VideoModel> {
        searchList.clear()

        return withContext(Dispatchers.IO) {
            val result = searchResult(query).items?.map { item ->
                VideoModel(
                    thumbnail = item.snippet?.thumbnails?.default?.url.toString(),
                    title =  item.snippet?.title.toString(),
                    description = item.snippet?.description.toString(),
                    channelTitle =  item.snippet?.channelTitle.toString(),
                    channelSubscript = "0",
                    publishedDate = item.snippet?.publishedAt.toString()
                )
            }
            if (result != null) {
                searchList = result.toMutableList()
            }
            searchList
        }
    }

    override suspend fun getPopularVideo(): MutableList<VideoModel> {
        popularList.clear()
        return withContext(Dispatchers.IO) {
            val items = YouTubeApi.youtubeNetwork.getVideo().items?.map { item ->
                VideoModel(
                    thumbnail = item.snippet?.thumbnails?.default?.url.toString(),
                    title = item.snippet?.title.toString(),
                    description = item.snippet?.description.toString(),
                    channelTitle = item.snippet?.channelTitle.toString(),
                    channelSubscript = "0",
                    publishedDate = item.snippet?.publishedAt.toString()
                )
            }
            if (items != null) {
                popularList = items.toMutableList()
            }
            popularList
        }
    }
}
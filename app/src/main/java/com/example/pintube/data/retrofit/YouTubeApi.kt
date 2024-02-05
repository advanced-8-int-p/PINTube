package com.example.pintube.data.retrofit

import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object YouTubeApi {
    private const val YOUTUBE_BASE_URL = "https://www.googleapis.com/youtube/v3/videos"

    private fun createOkHttpClient() : OkHttpClient {
        val connectionPool = ConnectionPool(5, 5, TimeUnit.MINUTES)

        val interceptor = HttpLoggingInterceptor()

        return OkHttpClient.Builder()
            .connectionPool(connectionPool)
            .addNetworkInterceptor(interceptor)
            .addInterceptor(YoutubeInterceptor())
            .build()
    }
}
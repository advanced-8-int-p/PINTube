package com.example.pintube.data.remote.api.retrofit

import com.example.pintube.BuildConfig
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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
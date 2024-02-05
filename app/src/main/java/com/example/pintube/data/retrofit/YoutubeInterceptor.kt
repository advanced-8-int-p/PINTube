package com.example.pintube.data.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class YoutubeInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val auth = ""
    }

}

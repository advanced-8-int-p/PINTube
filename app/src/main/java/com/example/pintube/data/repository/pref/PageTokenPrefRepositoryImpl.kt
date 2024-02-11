package com.example.pintube.data.repository.pref

import android.content.Context
import com.example.pintube.domain.repository.PageTokenPrefRepository

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
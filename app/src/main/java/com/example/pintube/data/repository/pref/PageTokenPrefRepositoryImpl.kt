package com.example.pintube.data.repository.pref

import android.content.Context
import android.util.Log
import com.example.pintube.domain.repository.PageTokenPrefRepository
import com.google.gson.Gson

class PageTokenPrefRepositoryImpl(context: Context) : PageTokenPrefRepository {

    private val pref = context.getSharedPreferences("PageToken", Context.MODE_PRIVATE)
    override fun saveNextPageToken(
        query: String,
        page: String,
        token: String?
    ) {
        pref.edit().putString(query + page, token).apply()
    }

    override fun getPageToken(query: String, page: String) = pref.getString(query + page, null)


    // 카테고리 SharedPreferences 도 그냥 여기다 추가
    private val gson = Gson()
    private val categoryPref = context.getSharedPreferences("categoryPref", Context.MODE_PRIVATE)
    override fun saveCategory(key: String, list: List<String>) {
        val json = gson.toJson(list)
        categoryPref.edit().putString(key, json).apply()
    }

    override fun loadCategory(key: String): List<String> {
        val json = categoryPref.getString(key, null) ?: return emptyList<String>().apply {
            Log.e("jj-PageTokenPrefRepositoryImpl getCategory", "pref.getString null. key: $key")
        }
        return gson.fromJson(json, Array<String>::class.java).toList()
    }

}

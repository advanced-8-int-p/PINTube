package com.example.pintube.ui.Search

import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

object SharedPrefManager {
    private const val SHARED_SEARCH_HISTORY = "share_search_history"

    private const val KEY_SEARCH_HISTORY = "key_search_history"


    private fun getSharedPreferences(context: Context) : SharedPreferences {
        return context.getSharedPreferences(SHARED_SEARCH_HISTORY, Context.MODE_PRIVATE)
    }

    fun saveSearchHistory(context: Context, searchQuery: String) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val searchHistorySet = HashSet<String>()

        // 기존의 검색 기록을 불러와 Set에 추가
        searchHistorySet.addAll(getSearchHistory(context))

        // 새로운 검색어를 Set에 추가
        searchHistorySet.add(searchQuery)

        // SharedPreferences에 저장
        editor.putStringSet(KEY_SEARCH_HISTORY, searchHistorySet)
        editor.apply()
    }

    // 검색 기록을 불러오는 메서드
    fun getSearchHistory(context: Context): Set<String> {
        val sharedPreferences = getSharedPreferences(context)
        return sharedPreferences.getStringSet(KEY_SEARCH_HISTORY, HashSet()) ?: HashSet()
    }

    // 검색 기록을 삭제하는 메서드
    fun clearSearchHistory(context: Context) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.remove(KEY_SEARCH_HISTORY)
        editor.apply()
    }
}

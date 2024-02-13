package com.example.pintube.data.repository.pref

import android.content.Context
import com.example.pintube.domain.repository.CategoryPrefRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CategoryPrefRepositoryImpl(context: Context): CategoryPrefRepository {

    private val pref = context.getSharedPreferences("CategoryList", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        const val CATEGORY = "Category"
    }

    override fun saveCategoryList(categoryList: List<String>) {
        val json = gson.toJson(categoryList)
        pref.edit().putString(CATEGORY, json).apply()
    }

    override fun getCategoryList(): List<String> {
        val json = pref.getString(CATEGORY, null)
        return if (json != null) {
            val type = object : TypeToken<List<String>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

}
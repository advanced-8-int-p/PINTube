package com.example.pintube.data.local.database

import androidx.room.TypeConverter
import com.example.pintube.data.local.entity.LocalCommentEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LocalTypeConverters {
    @TypeConverter
    fun toStringList(items: List<String>?): String? {
        return Gson().toJson(items)
    }

    @TypeConverter
    fun fromString(item: String?): List<String>? {
        val listType = object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(item, listType)
    }

    @TypeConverter
    fun fromCommentList(items: List<LocalCommentEntity>?): String {
        val gson = Gson()
        return gson.toJson(items)
    }

    @TypeConverter
    fun toCommentList(item: String): List<LocalCommentEntity>? {
        val listType = object : TypeToken<List<LocalCommentEntity?>?>() {}.type
        return Gson().fromJson(item, listType)
    }
}
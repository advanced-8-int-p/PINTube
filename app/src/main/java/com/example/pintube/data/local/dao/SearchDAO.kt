package com.example.pintube.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pintube.data.local.entity.LocalSearchEntity
import com.example.pintube.data.local.entity.LocalVideoEntity

@Dao
interface SearchDAO {
    @Insert
    fun insert(item: LocalSearchEntity)

    @Update
    fun update(item: LocalSearchEntity)

    @Delete
    fun delete(item: LocalSearchEntity)

    @Query("Select * From search_info Where `query` = :query")
    fun findSearchRecord(query: String): List<LocalSearchEntity>?
}
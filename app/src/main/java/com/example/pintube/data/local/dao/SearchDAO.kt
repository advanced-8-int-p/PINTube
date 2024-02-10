package com.example.pintube.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pintube.data.local.entity.LocalSearchEntity
import com.example.pintube.data.local.entity.LocalVideoEntity

@Dao
interface SearchDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: LocalSearchEntity)

    @Update
    fun update(item: LocalSearchEntity)

    @Delete
    fun delete(item: LocalSearchEntity)

    @Query("Select * From search_info Where `query` = :query AND saveDate >= :date")
    fun findSearchRecord(query: String, date: String): List<LocalSearchEntity>?
}
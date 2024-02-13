package com.example.pintube.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pintube.data.local.entity.RecentViewsEntity

@Dao
interface RecentViewsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: RecentViewsEntity)

    @Update
    fun update(item: RecentViewsEntity)

    @Delete
    fun delete(item: RecentViewsEntity)

    @Query("Select * From recent_view Where video_id = :id Order By save_date Desc")
    fun findRecentView(id: String): List<RecentViewsEntity>?
}
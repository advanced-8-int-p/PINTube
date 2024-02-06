package com.example.pintube.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pintube.data.local.entity.LocalChannelEntity
import com.example.pintube.data.local.entity.LocalVideoEntity

@Dao
interface ChannelDAO {
    @Insert
    fun insert(item: LocalChannelEntity)

    @Update
    fun update(item: LocalChannelEntity)

    @Delete
    fun delete(item: LocalChannelEntity)

    @Query("Select * From channel_info Where id = :id")
    fun findChannel(id: String): LocalVideoEntity?
}
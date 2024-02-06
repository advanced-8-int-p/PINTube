package com.example.pintube.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.pintube.data.local.entity.LocalChannelEntity

@Dao
interface ChannelDAO {
    @Insert
    fun insert(item: LocalChannelEntity)

    @Update
    fun update(item: LocalChannelEntity)

    @Delete
    fun delete(item: LocalChannelEntity)
}
package com.example.pintube.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.pintube.data.local.entity.VideoCacheEntity

@Dao
interface ContentsDAO {
    @Insert
    fun insert(item: VideoCacheEntity)

    @Update
    fun update(item: VideoCacheEntity)

    @Delete
    fun delete(item: VideoCacheEntity)

}
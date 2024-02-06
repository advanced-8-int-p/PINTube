package com.example.pintube.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.pintube.data.local.entity.LocalVideoEntity

@Dao
interface VideoDAO {
    @Insert
    fun insert(item: LocalVideoEntity)

    @Update
    fun update(item: LocalVideoEntity)

    @Delete
    fun delete(item: LocalVideoEntity)
}
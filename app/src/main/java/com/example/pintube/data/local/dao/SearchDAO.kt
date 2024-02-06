package com.example.pintube.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.pintube.data.local.entity.LocalSearchEntity

@Dao
interface SearchDAO {
    @Insert
    fun insert(item: LocalSearchEntity)

    @Update
    fun update(item: LocalSearchEntity)

    @Delete
    fun delete(item: LocalSearchEntity)
}
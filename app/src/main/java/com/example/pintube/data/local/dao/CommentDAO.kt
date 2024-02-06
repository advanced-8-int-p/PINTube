package com.example.pintube.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.example.pintube.data.local.entity.LocalCommentEntity

@Dao
interface CommentDAO {
    @Insert
    fun insert(item: LocalCommentEntity)

    @Update
    fun update(item: LocalCommentEntity)

    @Delete
    fun delete(item: LocalCommentEntity)
}
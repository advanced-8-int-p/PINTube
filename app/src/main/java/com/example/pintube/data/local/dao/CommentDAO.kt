package com.example.pintube.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.pintube.data.local.entity.LocalCommentEntity
import com.example.pintube.data.local.entity.LocalVideoEntity

@Dao
interface CommentDAO {
    @Insert
    fun insert(item: LocalCommentEntity)

    @Update
    fun update(item: LocalCommentEntity)

    @Delete
    fun delete(item: LocalCommentEntity)

    // 비디오 아이디 입력시 테이블에 저장된 댓글 목록 호출
    @Query("Select * From comment_info Where videoId = :id")
    fun findComment(id: String): LocalVideoEntity?
}
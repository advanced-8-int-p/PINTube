package com.example.pintube.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
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

    // 비디오 아이디 입력시 테이블에 저장된 상세 비디오 값 호출
    @Query("Select * From video_info Where id = :id")
    fun findVideo(id: String): LocalVideoEntity?
}
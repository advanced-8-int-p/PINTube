package com.example.pintube.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pintube.data.local.entity.LocalVideoEntity

@Dao
interface VideoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: LocalVideoEntity)

    @Update
    fun update(item: LocalVideoEntity)

    @Delete
    fun delete(item: LocalVideoEntity)

    // 비디오 아이디 입력시 테이블에 저장된 상세 비디오 값 호출
    @Query("Select * From video_info Where id = :id AND saveDate >= :date")
    fun findVideo(id: String, date: String): LocalVideoEntity?

    @Query("SELECT * FROM video_info WHERE isPopular = 1 AND saveDate >= :date")
    fun findPopularVideos(date: String): List<LocalVideoEntity>?
}

//fk pk
//레디시 캐시, 캐시 sql
//배포 환경, 오토스케일
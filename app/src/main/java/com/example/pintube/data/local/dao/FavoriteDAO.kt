package com.example.pintube.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pintube.data.local.entity.FavoriteCategoryCross
import com.example.pintube.data.local.entity.FavoriteEntity
import com.example.pintube.data.local.entity.LocalVideoEntity

@Dao
interface FavoriteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: FavoriteEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addFavoriteCategoryCross(cross: FavoriteCategoryCross)
    @Update
    fun update(item: FavoriteEntity)

    @Delete
    fun delete(item: FavoriteEntity)

    @Delete
    fun deleteFavoriteCategoryCross(cross: FavoriteCategoryCross)

    @Query(
        """
        SELECT * FROM favorite_info
         INNER JOIN favorite_category_cross
         ON favorite_info.video_id = favorite_category_cross.videoId 
         WHERE favorite_category_cross.categoryId = :categoryId
         """
    )
    fun getFavoritesByCategory(categoryId: Long): List<FavoriteEntity>

    @Query("Select * From favorite_info Where video_id = :id")
    fun checkBookmarkVideo(id: String): FavoriteEntity?

}
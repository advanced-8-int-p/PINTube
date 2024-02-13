package com.example.pintube.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pintube.data.local.entity.CategoryEntity

@Dao
interface CategoryDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(category: CategoryEntity)

    @Update
    fun update(category: CategoryEntity)

    @Query("SELECT * FROM categories WHERE name = :name")
    fun getCategoryByName(name: String): CategoryEntity?
}
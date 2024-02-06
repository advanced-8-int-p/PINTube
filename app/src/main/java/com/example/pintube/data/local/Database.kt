package com.example.pintube.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pintube.data.local.dao.ContentsDAO
import com.example.pintube.data.local.entity.FavoriteEntity
import com.example.pintube.data.local.entity.VideoCacheEntity

@Database(entities = [FavoriteEntity::class, VideoCacheEntity::class], version = 1)
abstract class Database : RoomDatabase() {
    abstract fun contentsDao(): ContentsDAO
}
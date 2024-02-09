package com.example.pintube.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pintube.data.local.dao.ChannelDAO
import com.example.pintube.data.local.dao.CommentDAO
import com.example.pintube.data.local.dao.SearchDAO
import com.example.pintube.data.local.dao.VideoDAO
import com.example.pintube.data.local.entity.FavoriteEntity
import com.example.pintube.data.local.entity.LocalChannelEntity
import com.example.pintube.data.local.entity.LocalCommentEntity
import com.example.pintube.data.local.entity.LocalSearchEntity
import com.example.pintube.data.local.entity.LocalVideoEntity

@Database(
    entities = [
        FavoriteEntity::class,
        LocalSearchEntity::class,
        LocalVideoEntity::class,
        LocalChannelEntity::class,
        LocalCommentEntity::class
               ],
    version = 4
)
@TypeConverters(LocalTypeConverters::class)
abstract class YoutubeDatabase : RoomDatabase() {
    abstract fun searchDao(): SearchDAO
    abstract fun videoDao(): VideoDAO
    abstract fun channelDao(): ChannelDAO
    abstract fun commentDao(): CommentDAO

}
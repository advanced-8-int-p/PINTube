package com.example.pintube.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pintube.data.local.dao.ChannelDAO
import com.example.pintube.data.local.dao.CommentDAO
import com.example.pintube.data.local.dao.ContentsDAO
import com.example.pintube.data.local.dao.SearchDAO
import com.example.pintube.data.local.dao.VideoDAO
import com.example.pintube.data.local.entity.LocalChannelEntity
import com.example.pintube.data.local.entity.LocalCommentEntity
import com.example.pintube.data.local.entity.FavoriteEntity
import com.example.pintube.data.local.entity.LocalSearchEntity
import com.example.pintube.data.local.entity.VideoCacheEntity
import com.example.pintube.data.local.entity.LocalVideoEntity

@Database(
    entities = [
        FavoriteEntity::class,
        VideoCacheEntity::class,
        LocalSearchEntity::class,
        LocalVideoEntity::class,
        LocalChannelEntity::class,
        LocalCommentEntity::class
               ],
    version = 1
)
@TypeConverters(LocalTypeConverters::class)
abstract class YoutubeDatabase : RoomDatabase() {
    abstract fun contentsDao(): ContentsDAO
    abstract fun searchDao(): SearchDAO
    abstract fun videoDao(): VideoDAO
    abstract fun channelDao(): ChannelDAO
    abstract fun commentDao(): CommentDAO

    companion object {
        private var instance: YoutubeDatabase? = null

        @Synchronized
        fun getInstance(context: Context): YoutubeDatabase? {
            if (instance == null) {
                synchronized(YoutubeDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        YoutubeDatabase::class.java,
                        "youtube-database"
                    ).build()
                }
            }
            return instance
        }
    }
}
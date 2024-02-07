package com.example.pintube.di

import android.content.Context
import androidx.room.Room
import com.example.pintube.data.local.dao.ChannelDAO
import com.example.pintube.data.local.dao.CommentDAO
import com.example.pintube.data.local.dao.SearchDAO
import com.example.pintube.data.local.dao.VideoDAO
import com.example.pintube.data.local.database.YoutubeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideSearchDao(database: YoutubeDatabase): SearchDAO = database.searchDao()

    @Singleton
    @Provides
    fun provideVideoDao(database: YoutubeDatabase): VideoDAO = database.videoDao()

    @Singleton
    @Provides
    fun provideChannelDao(database: YoutubeDatabase): ChannelDAO = database.channelDao()

    @Singleton
    @Provides
    fun provideCommentDao(database: YoutubeDatabase): CommentDAO = database.commentDao()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): YoutubeDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            YoutubeDatabase::class.java,
            "youtube-database"
        ).build()
}

package com.example.pintube.di

import android.content.Context
import com.example.pintube.domain.usecase.ConvertDurationTime
import com.example.pintube.domain.usecase.ConvertPublishedAt
import com.example.pintube.domain.usecase.ConvertToDaysAgo
import com.example.pintube.domain.usecase.ConvertViewCount
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object ConvertUsecaseModule {
    @Provides
    fun provideConvertDurationTime(): ConvertDurationTime = ConvertDurationTime()

    @Provides
    fun provideConvertPublishedAt(): ConvertPublishedAt = ConvertPublishedAt()

    @Provides
    fun provideConvertToDaysAgo(): ConvertToDaysAgo = ConvertToDaysAgo()

    @Provides
    fun provideConvertViewCount(): ConvertViewCount = ConvertViewCount()
}
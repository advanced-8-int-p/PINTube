package com.example.pintube.di

import com.example.pintube.data.repository.ApiRepositoryImpl
import com.example.pintube.data.repository.LocalChannelRepositoryImpl
import com.example.pintube.data.repository.LocalCommentRepositoryImpl
import com.example.pintube.data.repository.LocalSearchRepositoryImpl
import com.example.pintube.data.repository.LocalVideoRepositoryImpl
import com.example.pintube.domain.repository.ApiRepository
import com.example.pintube.domain.repository.LocalChannelRepository
import com.example.pintube.domain.repository.LocalCommentRepository
import com.example.pintube.domain.repository.LocalSearchRepository
import com.example.pintube.domain.repository.LocalVideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindApiRepository(repository: ApiRepositoryImpl): ApiRepository

    @Binds
    abstract fun bindLocalSearchRepository(repository: LocalSearchRepositoryImpl): LocalSearchRepository

    @Binds
    abstract fun bindLocalVideoRepository(repository: LocalVideoRepositoryImpl): LocalVideoRepository

    @Binds
    abstract fun bindLocalChannelRepository(repository: LocalChannelRepositoryImpl): LocalChannelRepository

    @Binds
    abstract fun bindLocalCommentRepository(repository: LocalCommentRepositoryImpl): LocalCommentRepository
}
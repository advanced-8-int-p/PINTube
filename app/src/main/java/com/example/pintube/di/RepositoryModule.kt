package com.example.pintube.di

import com.example.pintube.data.repository.sever.ApiRepositoryImpl
import com.example.pintube.data.repository.local.LocalChannelRepositoryImpl
import com.example.pintube.data.repository.local.LocalCommentRepositoryImpl
import com.example.pintube.data.repository.local.LocalSearchRepositoryImpl
import com.example.pintube.data.repository.local.LocalVideoRepositoryImpl
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
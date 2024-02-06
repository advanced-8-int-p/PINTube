package com.example.pintube.di

import com.example.pintube.data.repository.ApiRepositoryImpl
import com.example.pintube.domain.repository.ApiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindApiRepository(repository: ApiRepositoryImpl): ApiRepository
}
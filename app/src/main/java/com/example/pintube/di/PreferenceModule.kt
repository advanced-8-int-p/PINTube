package com.example.pintube.di

import android.content.Context
import com.example.pintube.data.repository.pref.CategoryPrefRepositoryImpl
import com.example.pintube.domain.repository.CategoryPrefRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object PreferenceModule {

    @Provides
    fun ProvideCategoryPrefRepository(@ApplicationContext context: Context): CategoryPrefRepository = CategoryPrefRepositoryImpl(context)
}
package com.example.remoteandroid.di

import com.example.remoteandroid.screens.applications.mappers.ApplicationsContentUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ApplicationsModule {

    @Provides
    fun provideApplicationsContentUiMapper(): ApplicationsContentUiMapper =
        ApplicationsContentUiMapper()
}
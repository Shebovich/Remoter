package com.example.remoteandroid.di

import com.example.remoteandroid.screens.remote.mappers.RemoteContentUiMapper
import com.example.remoteandroid.services.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    fun provideContentUiMapper(resourceProvider: ResourceProvider) : RemoteContentUiMapper {
        return RemoteContentUiMapper(resourceProvider)
    }
}
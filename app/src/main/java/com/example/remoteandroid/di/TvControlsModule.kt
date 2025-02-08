package com.example.remoteandroid.di

import com.example.remoteandroid.screens.tv.mappers.TvControlsContentUiMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TvControlsModule {

    @Provides
    fun provideTvControlsContentUiMapper() : TvControlsContentUiMapper {
        return TvControlsContentUiMapper()
    }
}
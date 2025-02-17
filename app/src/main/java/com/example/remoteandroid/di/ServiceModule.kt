package com.example.remoteandroid.di

import android.content.Context
import com.example.remoteandroid.data.mappers.ApplicationsMapper
import com.example.remoteandroid.services.ResourceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    fun provideResourceProvider(@ApplicationContext context: Context) : ResourceProvider =
        ResourceProvider(context)
}
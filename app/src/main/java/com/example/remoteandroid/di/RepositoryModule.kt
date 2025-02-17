package com.example.remoteandroid.di

import com.example.remoteandroid.data.ApplicationsRepository
import com.example.remoteandroid.data.DiscoveryRepository
import com.example.remoteandroid.data.RemoteRepository
import com.example.remoteandroid.data.mappers.ApplicationsMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideRemoteRepository() : RemoteRepository =
        RemoteRepository()

    @Provides
    fun provideDiscoveryRepository() : DiscoveryRepository =
        DiscoveryRepository()

    @Provides
    fun provideApplicationsRepository(applicationsMapper: ApplicationsMapper) : ApplicationsRepository =
        ApplicationsRepository(applicationsMapper)

    @Provides
    fun provideApplicationsMapper(): ApplicationsMapper = ApplicationsMapper()
}
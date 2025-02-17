package com.example.remoteandroid.di

import com.example.remoteandroid.data.ApplicationsRepository
import com.example.remoteandroid.data.DiscoveryRepository
import com.example.remoteandroid.data.RemoteRepository
import com.example.remoteandroid.domain.usecase.ConnectToDeviceUseCase
import com.example.remoteandroid.domain.usecase.FindDeviceUseCase
import com.example.remoteandroid.domain.usecase.GetApplicationsUseCase
import com.example.remoteandroid.domain.usecase.LaunchApplicationUseCase
import com.example.remoteandroid.domain.usecase.SendPairingCodeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideConnectToDeviceUseCase(repository: RemoteRepository): ConnectToDeviceUseCase =
        ConnectToDeviceUseCase(repository)

    @Provides
    fun provideFindDeviceUseCase(repository: DiscoveryRepository): FindDeviceUseCase =
        FindDeviceUseCase(repository)

    @Provides
    fun provideSendPairingCodeUseCase(repository: RemoteRepository): SendPairingCodeUseCase =
        SendPairingCodeUseCase(repository)

    @Provides
    fun providesGetApplicationsUseCase(repository: ApplicationsRepository): GetApplicationsUseCase =
        GetApplicationsUseCase(repository)

    @Provides
    fun provideLaunchApplicationUseCase(repository: ApplicationsRepository): LaunchApplicationUseCase =
        LaunchApplicationUseCase(repository)
}
package com.example.remoteandroid.di

import com.example.remoteandroid.data.DiscoveryRepository
import com.example.remoteandroid.data.RemoteRepository
import com.example.remoteandroid.domain.usecase.ConnectToDeviceUseCase
import com.example.remoteandroid.domain.usecase.FindDeviceUseCase
import com.example.remoteandroid.domain.usecase.GetConnectedDeviceUseCase
import com.example.remoteandroid.domain.usecase.SendPairingCodeUseCase
import com.example.remoteandroid.domain.usecase.SubscribeConnectionStateUseCase
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
    fun provideSendPairingCodeUseCase(repository: RemoteRepository) : SendPairingCodeUseCase =
        SendPairingCodeUseCase(repository)

    @Provides
    fun provideGetConnectedDeviceUseCase(repository: RemoteRepository) : GetConnectedDeviceUseCase =
        GetConnectedDeviceUseCase(repository)

    @Provides
    fun provideSubscribeConnectionStateUseCase(repository: RemoteRepository): SubscribeConnectionStateUseCase =
        SubscribeConnectionStateUseCase(repository)
}
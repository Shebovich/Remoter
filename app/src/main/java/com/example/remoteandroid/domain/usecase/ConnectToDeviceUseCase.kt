package com.example.remoteandroid.domain.usecase

import com.connectsdk.device.ConnectableDevice
import com.example.remoteandroid.data.RemoteRepository
import com.example.remoteandroid.domain.models.ConnectionState
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class ConnectToDeviceUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
) {

    suspend operator fun invoke(connectableDevice: ConnectableDevice) : SharedFlow<ConnectionState> {
        remoteRepository.connectToDevice(connectableDevice)
        return remoteRepository.connectionState
    }
}
package com.example.remoteandroid.domain.usecase

import com.connectsdk.device.ConnectableDevice
import com.example.remoteandroid.data.RemoteRepository
import com.example.remoteandroid.domain.models.ConnectionState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConnectToDeviceUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
) {

    operator fun invoke(connectableDevice: ConnectableDevice): Flow<ConnectionState> =
        remoteRepository.connectToDevice(connectableDevice)
}
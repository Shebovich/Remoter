package com.example.remoteandroid.domain.usecase

import com.connectsdk.device.ConnectableDevice
import com.example.remoteandroid.data.RemoteRepository
import javax.inject.Inject

class ConnectToDeviceUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
) {

    operator fun invoke(connectableDevice: ConnectableDevice) =
        remoteRepository.connectToDevice(connectableDevice)
}
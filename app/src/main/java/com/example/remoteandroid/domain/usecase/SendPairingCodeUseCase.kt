package com.example.remoteandroid.domain.usecase

import com.connectsdk.device.ConnectableDevice
import com.example.remoteandroid.data.RemoteRepository
import javax.inject.Inject

class SendPairingCodeUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository
) {

    operator fun invoke(selectedDevice: ConnectableDevice, code: String) =
        remoteRepository.enterPin(selectedDevice, code)
}
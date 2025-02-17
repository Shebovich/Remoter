package com.example.remoteandroid.domain.usecase

import com.connectsdk.device.ConnectableDevice
import com.example.remoteandroid.data.ApplicationsRepository
import javax.inject.Inject

class LaunchApplicationUseCase @Inject constructor(
    private val applicationsRepository: ApplicationsRepository,
) {

    operator fun invoke(connectableDevice: ConnectableDevice, id: String) =
        applicationsRepository.launchApplication(connectableDevice, id)
}
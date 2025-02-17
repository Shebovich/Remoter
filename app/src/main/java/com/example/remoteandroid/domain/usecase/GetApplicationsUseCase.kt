package com.example.remoteandroid.domain.usecase

import com.connectsdk.device.ConnectableDevice
import com.example.remoteandroid.data.ApplicationsRepository
import com.example.remoteandroid.domain.models.ConnectionState
import javax.inject.Inject

class GetApplicationsUseCase @Inject constructor(
    private val applicationsRepository: ApplicationsRepository,
) {

    operator fun invoke(connectionState: ConnectionState.Connected) =
        applicationsRepository.getApplications(connectionState)
}
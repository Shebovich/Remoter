package com.example.remoteandroid.domain.usecase

import com.example.remoteandroid.data.DiscoveryRepository
import com.example.remoteandroid.domain.models.DiscoveryState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FindDeviceUseCase @Inject constructor(
    private val discoveryRepository: DiscoveryRepository,
) {

    operator fun invoke(): Flow<DiscoveryState> =
        discoveryRepository.findDevice()
}
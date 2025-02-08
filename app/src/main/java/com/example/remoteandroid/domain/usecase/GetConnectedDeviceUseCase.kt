package com.example.remoteandroid.domain.usecase

import com.example.remoteandroid.data.RemoteRepository
import com.example.remoteandroid.domain.models.ConnectionState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetConnectedDeviceUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
) {

    operator fun invoke(): SharedFlow<ConnectionState> = remoteRepository.connectionState
}
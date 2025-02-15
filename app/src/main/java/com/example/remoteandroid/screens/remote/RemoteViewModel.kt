package com.example.remoteandroid.screens.remote

import androidx.lifecycle.ViewModel
import com.connectsdk.device.ConnectableDevice
import com.example.remoteandroid.domain.models.ConnectionState
import com.example.remoteandroid.domain.models.DiscoveryState
import com.example.remoteandroid.domain.usecase.ConnectToDeviceUseCase
import com.example.remoteandroid.domain.usecase.SendPairingCodeUseCase
import com.example.remoteandroid.screens.remote.mappers.RemoteContentUiMapper
import com.example.remoteandroid.screens.remote.models.RemotePayload
import com.example.remoteandroid.screens.remote.models.RemoteViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class RemoteViewModel @Inject constructor(
    private val contentUiMapper: RemoteContentUiMapper,
    private val sendPairingCodeUseCase: SendPairingCodeUseCase,
    private val connectToDeviceUseCase: ConnectToDeviceUseCase,
) : ViewModel() {

    private val viewPayload = MutableStateFlow(RemotePayload())

    val contentViewState: Flow<RemoteViewState> get() = _contentViewState
    private val _contentViewState = MutableStateFlow(currentContent)

    private val currentContent: RemoteViewState
        get() = contentUiMapper.toContent(viewPayload.value)


    fun enterPin(code: String) {
        viewPayload.value.selectedDevice?.let {
            sendPairingCodeUseCase(it, code.trim())
        }
    }

    private fun updateViewPayload(
        discoveryState: DiscoveryState = viewPayload.value.discoveryState,
        selectedDevice: ConnectableDevice? = viewPayload.value.selectedDevice,
        connectionState: ConnectionState = viewPayload.value.connectionState,
    ) {
        viewPayload.update {
            it.copy(
                selectedDevice = selectedDevice,
                discoveryState = discoveryState,
                connectionState = connectionState
            )
        }
        updateContent()
    }

    private fun updateContent() {
        _contentViewState.value = currentContent
    }

    fun onConnectionStateChanged(connectionState: ConnectionState) {
        println("remoteViewModel $connectionState")
        updateViewPayload(connectionState = connectionState)
    }

    fun onDiscoveryStateChanged(discoveryState: DiscoveryState) {
        updateViewPayload(discoveryState = discoveryState)
    }
}
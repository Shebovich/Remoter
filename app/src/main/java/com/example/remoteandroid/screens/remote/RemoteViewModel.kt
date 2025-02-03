package com.example.remoteandroid.screens.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectsdk.device.ConnectableDevice
import com.connectsdk.service.capability.MouseControl
import com.example.remoteandroid.domain.models.ConnectionState
import com.example.remoteandroid.domain.models.DiscoveryState
import com.example.remoteandroid.domain.usecase.ConnectToDeviceUseCase
import com.example.remoteandroid.domain.usecase.FindDeviceUseCase
import com.example.remoteandroid.domain.usecase.SendPairingCodeUseCase
import com.example.remoteandroid.screens.remote.mappers.RemoteContentUiMapper
import com.example.remoteandroid.screens.remote.models.MouseEvent
import com.example.remoteandroid.screens.remote.models.RemotePayload
import com.example.remoteandroid.screens.remote.models.RemoteViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteViewModel @Inject constructor(
    private val contentUiMapper: RemoteContentUiMapper,
    private val connectToDeviceUseCase: ConnectToDeviceUseCase,
    private val findDeviceUseCase: FindDeviceUseCase,
    private val sendPairingCodeUseCase: SendPairingCodeUseCase
) : ViewModel() {

    private val viewPayload = MutableStateFlow(RemotePayload())

    val contentViewState: Flow<RemoteViewState> get() = _contentViewState
    private val _contentViewState = MutableStateFlow(currentContent)

    private val currentContent: RemoteViewState
        get() = contentUiMapper.toContent(viewPayload.value)

    fun onCreateView() {
        viewModelScope.launch {
            findDeviceUseCase.invoke()
                .collect { state ->
                    updateViewPayload(discoveryState = state)
                }
        }
    }

    fun connectToDevice(selectedDevice: ConnectableDevice?) {
        if (selectedDevice == null) return
        updateViewPayload(selectedDevice = selectedDevice)
        viewModelScope.launch {
            connectToDeviceUseCase.invoke(selectedDevice)
                .collect { connectionState ->
                    updateViewPayload(connectionState = connectionState)
                }
        }
    }

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

    fun handleMouseEvent(mouseEvent: MouseEvent) {
        val state = getConnectedState() ?: return
        val mouseControl = state.device.getCapability(MouseControl::class.java)
        mouseControl.mouseControl
        when(mouseEvent) {
            MouseEvent.Click -> mouseControl.click()
            is MouseEvent.Move -> mouseControl.move(mouseEvent.dx, mouseEvent.dy)
            is MouseEvent.Scroll -> mouseControl.scroll(mouseEvent.dx, mouseEvent.dy)
        }
    }

    private fun getConnectedState() : ConnectionState.Connected? =
        viewPayload.value.connectionState as? ConnectionState.Connected
}
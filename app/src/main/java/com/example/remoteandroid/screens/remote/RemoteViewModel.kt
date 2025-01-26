package com.example.remoteandroid.screens.remote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectsdk.device.ConnectableDevice
import com.example.remoteandroid.domain.models.ConnectionState
import com.example.remoteandroid.domain.models.DiscoveryState
import com.example.remoteandroid.domain.usecase.ConnectToDeviceUseCase
import com.example.remoteandroid.domain.usecase.FindDeviceUseCase
import com.example.remoteandroid.screens.remote.mappers.RemoteContentUiMapper
import com.example.remoteandroid.screens.remote.models.RemotePayload
import com.example.remoteandroid.screens.remote.models.RemoteScreenState
import com.example.remoteandroid.screens.remote.models.RemoteViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteViewModel @Inject constructor(
    private val contentUiMapper: RemoteContentUiMapper,
    private val connectToDeviceUseCase: ConnectToDeviceUseCase,
    private val findDeviceUseCase: FindDeviceUseCase,
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
                    updateViewPayload(
                        screenState = RemoteScreenState.Searching(
                            discoveryState = state
                        )
                    )
                }
        }
    }

    fun connectToDevice(selectedDevice: ConnectableDevice) {
        updateViewPayload(screenState = RemoteScreenState.Connecting(selectedDevice = selectedDevice))
        viewModelScope.launch {
            connectToDeviceUseCase.invoke(selectedDevice)
                .collect { connectionState ->
                    updateViewPayload(
                        screenState = RemoteScreenState.Connecting(
                            selectedDevice = selectedDevice,
                            connectionState = connectionState
                        )
                    )
                }
        }
    }

    private fun updateViewPayload(
        screenState: RemoteScreenState = viewPayload.value.screenState,
    ) {
        viewPayload.update {
            it.copy(
                screenState = screenState
            )
        }
        updateContent()
    }

    private fun updateContent() {
        _contentViewState.value = currentContent
    }
}
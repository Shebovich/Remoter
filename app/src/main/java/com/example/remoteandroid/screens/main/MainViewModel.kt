package com.example.remoteandroid.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectsdk.device.ConnectableDevice
import com.connectsdk.discovery.DiscoveryManager
import com.example.remoteandroid.domain.models.ConnectionState
import com.example.remoteandroid.domain.models.DiscoveryState
import com.example.remoteandroid.domain.usecase.ConnectToDeviceUseCase
import com.example.remoteandroid.domain.usecase.FindDeviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val findDeviceUseCase: FindDeviceUseCase,
    private val connectToDeviceUseCase: ConnectToDeviceUseCase
) : ViewModel() {

    private val _discoveryState: MutableSharedFlow<DiscoveryState> =
        MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val discoveryState: SharedFlow<DiscoveryState> = _discoveryState

    private val _connectionState: MutableSharedFlow<ConnectionState> =
        MutableSharedFlow(
            replay = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val connectionState: SharedFlow<ConnectionState> = _connectionState

    fun onStart() {
        _discoveryState.tryEmit(DiscoveryState.Searching)
        viewModelScope.launch(Dispatchers.Main) {
            findDeviceUseCase.invoke()
                .collect { state ->
                    _discoveryState.tryEmit(state)
                }
        }
    }

    fun onNetworkAvailable() {
        onStart()
    }

    fun onNetworkLost() {
        _connectionState.tryEmit(ConnectionState.NetworkError)
        _discoveryState.tryEmit(DiscoveryState.NetworkError)
    }

    fun connectToDevice(selectedDevice: ConnectableDevice?) {
        if (selectedDevice == null) return
        viewModelScope.launch(Dispatchers.Main) {
            connectToDeviceUseCase.invoke(selectedDevice)
                .collect { connectionState ->
                    _connectionState.tryEmit(connectionState)
                }
        }
    }
}
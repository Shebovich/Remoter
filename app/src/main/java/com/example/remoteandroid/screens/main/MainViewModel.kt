package com.example.remoteandroid.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.remoteandroid.domain.models.ConnectionState
import com.example.remoteandroid.domain.models.DiscoveryState
import com.example.remoteandroid.domain.usecase.ConnectToDeviceUseCase
import com.example.remoteandroid.domain.usecase.FindDeviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val connectToDeviceUseCase: ConnectToDeviceUseCase,
    private val findDeviceUseCase: FindDeviceUseCase,
) : ViewModel() {

    private val _discoveryState: MutableStateFlow<DiscoveryState> = MutableStateFlow(DiscoveryState.Searching)
    val discoveryState: StateFlow<DiscoveryState> = _discoveryState

    fun onStart() {
        _discoveryState.value = DiscoveryState.Searching
        viewModelScope.launch {
            findDeviceUseCase.invoke()
                .collect { state ->
                    _discoveryState.update { state }
                }
        }
    }

    fun onNetworkAvailable() {
        if (discoveryState.value != DiscoveryState.NetworkError) return
        onStart()
    }

    fun onNetworkLost() {
        _discoveryState.value = DiscoveryState.NetworkError
    }
}
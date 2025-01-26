package com.example.remoteandroid.screens.remote.models

import com.connectsdk.device.ConnectableDevice
import com.example.remoteandroid.domain.models.ConnectionState
import com.example.remoteandroid.domain.models.DiscoveryState

sealed class RemoteScreenState {

    data class Searching(
        val discoveryState: DiscoveryState = DiscoveryState.Searching,
    ): RemoteScreenState()

    data class Connecting(
        val selectedDevice: ConnectableDevice,
        val connectionState: ConnectionState = ConnectionState.Waiting,
    ): RemoteScreenState()
}
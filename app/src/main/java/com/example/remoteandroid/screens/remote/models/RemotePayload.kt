package com.example.remoteandroid.screens.remote.models

import com.connectsdk.device.ConnectableDevice
import com.example.remoteandroid.domain.models.ConnectionState
import com.example.remoteandroid.domain.models.DiscoveryState

data class RemotePayload(
    val discoveryState: DiscoveryState = DiscoveryState.Searching,
    val selectedDevice: ConnectableDevice? = null,
    val connectionState: ConnectionState = ConnectionState.Waiting,
)

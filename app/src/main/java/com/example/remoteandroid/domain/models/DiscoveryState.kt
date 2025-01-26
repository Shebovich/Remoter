package com.example.remoteandroid.domain.models

import com.connectsdk.device.ConnectableDevice
import com.connectsdk.service.command.ServiceCommandError

sealed class DiscoveryState {

    data class Failed(val error: ServiceCommandError) : DiscoveryState()

    data class Added(val device: List<ConnectableDevice>) : DiscoveryState()

    data class Updated(val device: List<ConnectableDevice>) : DiscoveryState()

    data class Removed(val device: List<ConnectableDevice>) : DiscoveryState()

    data object Searching : DiscoveryState()
}
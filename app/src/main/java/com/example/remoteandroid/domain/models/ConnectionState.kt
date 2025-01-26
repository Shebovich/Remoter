package com.example.remoteandroid.domain.models

import com.connectsdk.device.ConnectableDevice
import com.connectsdk.service.DeviceService.PairingType

sealed class ConnectionState() {

    data class Connected(val device: ConnectableDevice) : ConnectionState()

    data object Failed : ConnectionState()

    data object Disconnected : ConnectionState()

    data class PairingRequired(val type: PairingType) : ConnectionState()

    data object Waiting : ConnectionState()
}

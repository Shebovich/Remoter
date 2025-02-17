package com.example.remoteandroid.domain.models

import com.connectsdk.device.ConnectableDevice
import com.connectsdk.service.DeviceService.PairingType
import com.connectsdk.service.capability.KeyControl
import com.connectsdk.service.capability.Launcher
import com.connectsdk.service.capability.MouseControl
import com.connectsdk.service.capability.PowerControl
import com.connectsdk.service.capability.TVControl
import com.connectsdk.service.capability.VolumeControl

sealed class ConnectionState() {

    data class Connected(
        val device: ConnectableDevice,
        val powerControl: PowerControl? = null,
        val tvControl: TVControl? = null,
        val volumeControl: VolumeControl? = null,
        val launcher: Launcher? = null,
        val keyControl: KeyControl? = null,
        val mouseControl: MouseControl? = null
    ) : ConnectionState()

    data object Failed : ConnectionState()

    data object Disconnected : ConnectionState()

    data class PairingRequired(val type: PairingType) : ConnectionState()

    data object Waiting : ConnectionState()

    data object NetworkError : ConnectionState()
}

package com.example.remoteandroid.screens.tv.models

import com.connectsdk.device.ConnectableDevice
import com.connectsdk.service.capability.KeyControl
import com.connectsdk.service.capability.Launcher
import com.connectsdk.service.capability.MouseControl
import com.connectsdk.service.capability.PowerControl
import com.connectsdk.service.capability.TVControl
import com.connectsdk.service.capability.VolumeControl

data class TvControlsPayload(
    val connectedDevice: ConnectableDevice? = null,
    val isMuted: Boolean = false,
    val powerControl: PowerControl? = null,
    val tvControl: TVControl? = null,
    val volumeControl: VolumeControl? = null,
    val launcher: Launcher? = null,
    val keyControl: KeyControl? = null,
    val mouseControl: MouseControl? = null
)

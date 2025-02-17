package com.example.remoteandroid.screens.tv.models

import com.connectsdk.device.ConnectableDevice
import com.connectsdk.service.capability.KeyControl
import com.connectsdk.service.capability.Launcher
import com.connectsdk.service.capability.MouseControl
import com.connectsdk.service.capability.PowerControl
import com.connectsdk.service.capability.TVControl
import com.connectsdk.service.capability.VolumeControl
import com.example.remoteandroid.domain.models.ConnectionState

data class TvControlsPayload(
    val connectionState: ConnectionState = ConnectionState.Waiting,
    val isMuted: Boolean = false
)

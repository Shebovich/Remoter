package com.example.remoteandroid.screens.tv.models

import com.connectsdk.device.ConnectableDevice

data class TvControlsPayload(
    val connectedDevice: ConnectableDevice? = null,
    val isMuted: Boolean = false
)

package com.example.remoteandroid.screens.remote.uidata

import com.connectsdk.device.ConnectableDevice
import com.example.remoteandroid.ui.recycler.UiData

data class TestFindingDeviceUiData(
    val title: String,
    val device: ConnectableDevice? = null
) : UiData {

    override fun getDiffId(): String = title
}

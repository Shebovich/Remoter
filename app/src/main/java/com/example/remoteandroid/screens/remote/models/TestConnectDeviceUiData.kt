package com.example.remoteandroid.screens.remote.models

import com.connectsdk.device.ConnectableDevice
import com.example.remoteandroid.ui.recycler.UiData

data class TestConnectDeviceUiData(
    val title: String,
    val id: String = "TestConnectDeviceUiData",
    val device: ConnectableDevice?
) : UiData {

    override fun getDiffId(): String = id
}

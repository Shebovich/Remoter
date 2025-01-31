package com.example.remoteandroid.screens.remote.models

import com.example.remoteandroid.ui.recycler.UiData

data class TestConnectDeviceUiData(
    val title: String,
    val id: String = "TestConnectDeviceUiData"
) : UiData {

    override fun getDiffId(): String = id
}

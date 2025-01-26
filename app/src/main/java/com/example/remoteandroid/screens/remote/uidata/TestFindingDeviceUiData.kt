package com.example.remoteandroid.screens.remote.uidata

import com.example.remoteandroid.ui.recycler.UiData

data class TestFindingDeviceUiData(
    val title: String,
) : UiData {

    override fun getDiffId(): String = title
}

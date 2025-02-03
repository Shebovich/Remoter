package com.example.remoteandroid.screens.remote.uidata

import com.example.remoteandroid.ui.recycler.UiData

class TouchPadUiData(
    val id: String = "TouchPadUiData"
) : UiData {

    override fun getDiffId(): String = id
}
package com.example.remoteandroid.screens.remote.uidata

import com.example.remoteandroid.ui.recycler.UiData

class TopControlsUiData(
    val id: String
) : UiData {

    override fun getDiffId(): String = id
}
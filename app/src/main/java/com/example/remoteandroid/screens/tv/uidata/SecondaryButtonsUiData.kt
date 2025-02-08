package com.example.remoteandroid.screens.tv.uidata

import com.example.remoteandroid.ui.recycler.UiData

data class SecondaryButtonsUiData(
    private val id: String = "SecondaryButtonsUiData",
) : UiData {
    override fun getDiffId(): String = id
}

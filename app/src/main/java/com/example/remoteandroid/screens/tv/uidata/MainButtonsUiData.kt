package com.example.remoteandroid.screens.tv.uidata

import com.example.remoteandroid.ui.recycler.UiData

data class MainButtonsUiData(
    private val id: String = "MainButtonsUiData",
) : UiData {

    override fun getDiffId(): String = id
}
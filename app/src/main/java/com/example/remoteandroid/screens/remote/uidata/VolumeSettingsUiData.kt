package com.example.remoteandroid.screens.remote.uidata

import com.example.remoteandroid.ui.recycler.UiData

data class VolumeSettingsUiData(
    private val id: String
) : UiData {

    override fun getDiffId(): String = id
}
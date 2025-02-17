package com.example.remoteandroid.screens.applications.uidata

import com.example.remoteandroid.domain.models.ApplicationInfo
import com.example.remoteandroid.ui.recycler.UiData

data class ApplicationUiData(
    val applicationInfo: ApplicationInfo
): UiData {
    override fun getDiffId(): String = applicationInfo.id
}
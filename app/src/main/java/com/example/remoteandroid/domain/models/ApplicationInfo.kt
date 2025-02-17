package com.example.remoteandroid.domain.models

import com.connectsdk.service.command.ServiceCommandError

sealed class ApplicationsState {
    data class Success(val data: List<ApplicationInfo>) : ApplicationsState()

    data class Error(val error: ServiceCommandError?) : ApplicationsState()

    data object Loading: ApplicationsState()
}

data class ApplicationInfo(
    val id: String,
    val name: String,
    val avatarUrl: String
)
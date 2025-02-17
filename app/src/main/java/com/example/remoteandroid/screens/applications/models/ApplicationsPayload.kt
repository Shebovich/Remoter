package com.example.remoteandroid.screens.applications.models

import com.example.remoteandroid.domain.models.ApplicationsState
import com.example.remoteandroid.domain.models.ConnectionState

data class ApplicationsPayload(
    val state: ApplicationsState = ApplicationsState.Loading,
    val connectionState: ConnectionState = ConnectionState.Waiting
)

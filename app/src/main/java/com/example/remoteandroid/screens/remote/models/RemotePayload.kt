package com.example.remoteandroid.screens.remote.models

data class RemotePayload(
    val screenState: RemoteScreenState = RemoteScreenState.Searching()
)

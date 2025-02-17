package com.example.remoteandroid.screens.applications

import com.example.remoteandroid.domain.models.ApplicationInfo
import com.example.remoteandroid.screens.applications.delegates.ApplicationsAdapterDelegate
import com.example.remoteandroid.ui.recycler.RecyclerAdapterDelegated


class ApplicationsAdapter(
    onApplicationClicked : (ApplicationInfo) -> Unit
) : RecyclerAdapterDelegated(
    ApplicationsAdapterDelegate(onApplicationClicked)
)
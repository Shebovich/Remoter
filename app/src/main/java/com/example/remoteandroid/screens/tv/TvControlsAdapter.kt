package com.example.remoteandroid.screens.tv

import com.example.remoteandroid.screens.remote.delegates.TouchPadAdapterDelegate
import com.example.remoteandroid.screens.remote.models.MouseEvent
import com.example.remoteandroid.screens.tv.delegates.MainButtonsAdapterDelegate
import com.example.remoteandroid.screens.tv.delegates.SecondaryButtonsAdapterDelegate
import com.example.remoteandroid.screens.tv.models.ButtonId
import com.example.remoteandroid.ui.recycler.RecyclerAdapterDelegated

class TvControlsAdapter(
    onButtonClicked: (ButtonId) -> Unit,
    onMouseEvent: (MouseEvent) -> Unit
) : RecyclerAdapterDelegated(
    MainButtonsAdapterDelegate(onButtonClicked),
    SecondaryButtonsAdapterDelegate(onButtonClicked),
    TouchPadAdapterDelegate(onMouseEvent, onButtonClicked)
)
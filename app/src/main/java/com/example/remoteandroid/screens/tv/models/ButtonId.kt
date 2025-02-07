package com.example.remoteandroid.screens.tv.models

import android.view.View

enum class ButtonId {
    OFF, BROWSER, SEARCH, EXIT, CHANNEL_UP, CHANNEL_DOWN,
    CHANNEL_ONE, LIST_CHANNELS, VOLUME_UP, VOLUME_DOWN,
    HOME, MUTE
}

fun View.onTvButtonClicked(buttonId: ButtonId, onButtonClicked: (ButtonId) -> Unit) {
    setOnClickListener {
        onButtonClicked.invoke(buttonId)
    }
}
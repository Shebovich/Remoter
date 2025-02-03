package com.example.remoteandroid.screens.remote.models

sealed class MouseEvent {

    data object Click : MouseEvent()

    data class Scroll(
        val dx: Double,
        val dy: Double
    ): MouseEvent()

    data class Move(
        val dx: Double,
        val dy: Double
    ): MouseEvent()
}
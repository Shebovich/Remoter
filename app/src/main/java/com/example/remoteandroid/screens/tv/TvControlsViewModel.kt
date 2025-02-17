package com.example.remoteandroid.screens.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.connectsdk.device.ConnectableDevice
import com.connectsdk.service.capability.KeyControl
import com.connectsdk.service.capability.Launcher
import com.connectsdk.service.capability.MouseControl
import com.connectsdk.service.capability.PowerControl
import com.connectsdk.service.capability.TVControl
import com.connectsdk.service.capability.VolumeControl
import com.example.remoteandroid.data.RemoteRepository
import com.example.remoteandroid.domain.models.ConnectionState
import com.example.remoteandroid.screens.remote.models.MouseEvent
import com.example.remoteandroid.screens.tv.mappers.TvControlsContentUiMapper
import com.example.remoteandroid.screens.tv.models.ButtonId
import com.example.remoteandroid.screens.tv.models.TvControlsPayload
import com.example.remoteandroid.screens.tv.models.TvControlsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvControlsViewModel @Inject constructor(
    private val contentUiMapper: TvControlsContentUiMapper,
) : ViewModel() {

    private val viewPayload = MutableStateFlow(TvControlsPayload())

    val contentViewState: Flow<TvControlsViewState> get() = _contentViewState
    private val _contentViewState = MutableStateFlow(currentContent)

    private val currentContent: TvControlsViewState
        get() = contentUiMapper.toContent(viewPayload.value)

    private fun muteClicked() {
        val connectedState = getConnectedState(viewPayload.value.connectionState) ?: return
        val muteSet = !viewPayload.value.isMuted
        connectedState.volumeControl?.setMute(muteSet, null)
        viewPayload.update { it.copy(isMuted = muteSet) }
    }

    fun onViewCreated() {
        println("onViewCreated")
        _contentViewState.value = currentContent
    }

    fun onConnectionStateChanged(connectionState: ConnectionState) {
        println("onConnectionStateChanged $connectionState")
        viewPayload.update {
            it.copy(
                connectionState = connectionState
            )
        }
    }

    fun onButtonClicked(buttonId: ButtonId) {
        println("onButtonClicked : $buttonId")
        val connectedState = getConnectedState(viewPayload.value.connectionState) ?: return
        when (buttonId) {
            ButtonId.OFF -> connectedState.powerControl?.powerOff(null)
            ButtonId.BROWSER -> connectedState.launcher?.launchBrowser(
                "http://google.com/",
                null
            )

            ButtonId.SEARCH -> Unit
            ButtonId.EXIT -> connectedState.keyControl?.home(null)
            ButtonId.CHANNEL_UP -> connectedState.tvControl?.channelUp(null)
            ButtonId.CHANNEL_DOWN -> connectedState.tvControl?.channelDown(null)
            ButtonId.CHANNEL_ONE -> Unit
            ButtonId.LIST_CHANNELS -> Unit
            ButtonId.VOLUME_UP -> connectedState.volumeControl?.volumeUp(null)
            ButtonId.VOLUME_DOWN -> connectedState.volumeControl?.volumeDown(null)
            ButtonId.HOME -> connectedState.keyControl?.home(null)
            ButtonId.MUTE -> muteClicked()
            ButtonId.MOUSE_LEFT -> connectedState.keyControl?.left(null)
            ButtonId.MOUSE_RIGHT -> connectedState.keyControl?.right(null)
            ButtonId.MOUSE_TOP -> connectedState.keyControl?.up(null)
            ButtonId.MOUSE_BOTTOM -> connectedState.keyControl?.down(null)
            ButtonId.BACK -> connectedState.keyControl?.back(null)
        }
    }

    fun onMouseEvent(mouseEvent: MouseEvent) {
        println("mouseEvent : $mouseEvent")
        val connectedState = getConnectedState(viewPayload.value.connectionState) ?: return
        when (mouseEvent) {
            MouseEvent.Click -> connectedState.mouseControl?.click()
            is MouseEvent.Move -> connectedState.mouseControl?.move(mouseEvent.dx, mouseEvent.dy)
            is MouseEvent.Scroll -> connectedState.mouseControl?.scroll(
                mouseEvent.dx,
                mouseEvent.dy
            )
        }
    }

    private fun getConnectedState(connectionState: ConnectionState): ConnectionState.Connected? {
        return (connectionState as? ConnectionState.Connected)
    }
}
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
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    private val viewPayload = MutableStateFlow(TvControlsPayload())

    val contentViewState: Flow<TvControlsViewState> get() = _contentViewState
    private val _contentViewState = MutableStateFlow(currentContent)

    private val currentContent: TvControlsViewState
        get() = contentUiMapper.toContent(viewPayload.value)

    private fun muteClicked() {
        val muteSet = !viewPayload.value.isMuted
        viewPayload.value.volumeControl?.setMute(muteSet, null)
        viewPayload.update { it.copy(isMuted = muteSet) }
    }

    fun onViewCreated() {
        println("onViewCreated")
        _contentViewState.value = currentContent
    }

    fun onConnectionStateChanged(connectionState: ConnectionState) {
        println("onConnectionStateChanged $connectionState")
        if (connectionState is ConnectionState.Connected) {
            onDeviceConnected(connectionState.device)
        } else {
            viewPayload.update { it.copy(connectedDevice = null) }
        }
    }

    fun onButtonClicked(buttonId: ButtonId) {
        println("onButtonClicked : $buttonId")
        when (buttonId) {
            ButtonId.OFF -> viewPayload.value.powerControl?.powerOff(null)
            ButtonId.BROWSER -> viewPayload.value.launcher?.launchBrowser(
                "http://google.com/",
                null
            )

            ButtonId.SEARCH -> Unit
            ButtonId.EXIT -> viewPayload.value.keyControl?.home(null)
            ButtonId.CHANNEL_UP -> viewPayload.value.tvControl?.channelUp(null)
            ButtonId.CHANNEL_DOWN -> viewPayload.value.tvControl?.channelDown(null)
            ButtonId.CHANNEL_ONE -> Unit
            ButtonId.LIST_CHANNELS -> Unit
            ButtonId.VOLUME_UP -> viewPayload.value.volumeControl?.volumeUp(null)
            ButtonId.VOLUME_DOWN -> viewPayload.value.volumeControl?.volumeDown(null)
            ButtonId.HOME -> viewPayload.value.keyControl?.home(null)
            ButtonId.MUTE -> muteClicked()
            ButtonId.MOUSE_LEFT -> viewPayload.value.keyControl?.left(null)
            ButtonId.MOUSE_RIGHT -> viewPayload.value.keyControl?.right(null)
            ButtonId.MOUSE_TOP -> viewPayload.value.keyControl?.up(null)
            ButtonId.MOUSE_BOTTOM -> viewPayload.value.keyControl?.down(null)
            ButtonId.BACK -> viewPayload.value.keyControl?.back(null)
        }
    }

    private fun onDeviceConnected(connectableDevice: ConnectableDevice) {
        println("onDeviceConnected : $connectableDevice")
        viewPayload.update {
            it.copy(
                connectedDevice = connectableDevice,
                powerControl = connectableDevice.getCapability(PowerControl::class.java),
                volumeControl = connectableDevice.getCapability(VolumeControl::class.java),
                tvControl = connectableDevice.getCapability(TVControl::class.java),
                launcher = connectableDevice.getCapability(Launcher::class.java),
                keyControl = connectableDevice.getCapability(KeyControl::class.java),
                mouseControl = connectableDevice.getCapability(MouseControl::class.java)
            )
        }
    }

    fun onMouseEvent(mouseEvent: MouseEvent) {
        println("mouseEvent : $mouseEvent")
        when (mouseEvent) {
            MouseEvent.Click -> viewPayload.value.mouseControl?.click()
            is MouseEvent.Move -> viewPayload.value.mouseControl?.move(mouseEvent.dx, mouseEvent.dy)
            is MouseEvent.Scroll -> viewPayload.value.mouseControl?.scroll(
                mouseEvent.dx,
                mouseEvent.dy
            )
        }
    }
}
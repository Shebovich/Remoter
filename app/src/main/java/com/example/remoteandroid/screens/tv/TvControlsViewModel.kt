package com.example.remoteandroid.screens.tv

import androidx.lifecycle.ViewModel
import com.connectsdk.service.capability.ExternalInputControl
import com.connectsdk.service.capability.KeyControl
import com.connectsdk.service.capability.Launcher
import com.connectsdk.service.capability.PowerControl
import com.connectsdk.service.capability.TVControl
import com.connectsdk.service.capability.ToastControl
import com.connectsdk.service.capability.VolumeControl
import com.example.remoteandroid.screens.remote.models.RemotePayload
import com.example.remoteandroid.screens.remote.models.RemoteViewState
import com.example.remoteandroid.screens.tv.models.ButtonId
import com.example.remoteandroid.screens.tv.models.TvControlsPayload
import com.example.remoteandroid.screens.tv.models.TvControlsViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TvControlsViewModel @Inject constructor(

) : ViewModel() {

    private val viewPayload = MutableStateFlow(TvControlsPayload())

    val contentViewState: Flow<TvControlsViewState> get() = _contentViewState
    private val _contentViewState = MutableStateFlow(currentContent)

    private val currentContent: TvControlsViewState
        get() = contentUiMapper.toContent(viewPayload.value)

    private fun muteClicked() {
        val muteSet = !viewPayload.value.isMuted
        viewPayload.value.connectedDevice?.getCapability(VolumeControl::class.java)
            ?.setMute(muteSet, null)
        viewPayload.update { it.copy(isMuted = muteSet) }
    }

    fun onButtonClicked(buttonId: ButtonId) {
        when (buttonId) {
            ButtonId.OFF -> viewPayload.value.connectedDevice?.getCapability(PowerControl::class.java)
                ?.powerOff(null)

            ButtonId.BROWSER -> viewPayload.value.connectedDevice?.getCapability(Launcher::class.java)
                ?.launchBrowser("http://google.com/", null)

            ButtonId.SEARCH -> Unit
            ButtonId.EXIT -> Unit
            ButtonId.CHANNEL_UP -> viewPayload.value.connectedDevice?.getCapability(TVControl::class.java)?.channelUp(null)
            ButtonId.CHANNEL_DOWN -> viewPayload.value.connectedDevice?.getCapability(TVControl::class.java)?.channelDown(null)
            ButtonId.CHANNEL_ONE -> Unit
            ButtonId.LIST_CHANNELS -> Unit
            ButtonId.VOLUME_UP -> viewPayload.value.connectedDevice?.getCapability(VolumeControl::class.java)
                ?.volumeUp(null)

            ButtonId.VOLUME_DOWN -> viewPayload.value.connectedDevice?.getCapability(VolumeControl::class.java)
                ?.volumeDown(null)

            ButtonId.HOME -> viewPayload.value.connectedDevice?.getCapability(KeyControl::class.java)
                ?.home(null)

            ButtonId.MUTE -> muteClicked()
        }
    }


}
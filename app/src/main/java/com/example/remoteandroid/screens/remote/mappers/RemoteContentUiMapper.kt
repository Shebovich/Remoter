package com.example.remoteandroid.screens.remote.mappers

import com.example.remoteandroid.domain.models.ConnectionState
import com.example.remoteandroid.domain.models.DiscoveryState
import com.example.remoteandroid.screens.remote.models.RemotePayload
import com.example.remoteandroid.screens.remote.models.RemoteScreenState
import com.example.remoteandroid.screens.remote.models.RemoteViewState
import com.example.remoteandroid.screens.remote.uidata.TopControlsUiData
import com.example.remoteandroid.screens.remote.uidata.TouchPadUiData
import com.example.remoteandroid.screens.remote.uidata.VolumeSettingsUiData
import com.example.remoteandroid.services.ResourceProvider
import com.example.remoteandroid.ui.recycler.UiData
import com.example.remoteandroid.ui.recycler.addUiItem
import com.example.remoteandroid.ui.recycler.createUiDataList
import javax.inject.Inject

class RemoteContentUiMapper @Inject constructor(
    resourceProvider: ResourceProvider
) {

    fun toContent(payload: RemotePayload) : RemoteViewState {
        return RemoteViewState(
            content = createUiDataList {
                addUiItem { mapToFindDeviceUiData(payload) }
                addUiItem { TopControlsUiData(id = TOP_CONTROLS_ID) }
                addUiItem { VolumeSettingsUiData(id = VOLUME_SETTINGS_ID) }
                addUiItem { TouchPadUiData(id = TOUCH_PAD_ID) }
            }
        )
    }

    private fun mapToFindDeviceUiData(payload: RemotePayload): UiData {
        var title: Int = 0
        when(val screenState = payload.screenState) {
            is RemoteScreenState.Connecting -> {
                when(screenState.connectionState) {
                    is ConnectionState.Connected -> TODO()
                    ConnectionState.Disconnected -> TODO()
                    ConnectionState.Failed -> TODO()
                    is ConnectionState.PairingRequired -> TODO()
                    ConnectionState.Waiting -> TODO()
                }
            }
            is RemoteScreenState.Searching -> {
                when(screenState.discoveryState) {
                    is DiscoveryState.Added -> TODO()
                    is DiscoveryState.Failed -> TODO()
                    is DiscoveryState.Removed -> TODO()
                    DiscoveryState.Searching -> TODO()
                    is DiscoveryState.Updated -> TODO()
                }
            }
        }
    }

    companion object {
        private const val TOP_CONTROLS_ID = "TOP_CONTROLS_ID"
        private const val VOLUME_SETTINGS_ID = "VOLUME_SETTINGS_ID"
        private const val TOUCH_PAD_ID = "TOUCH_PAD_ID"
    }
}
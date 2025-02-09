package com.example.remoteandroid.screens.remote.mappers

import com.connectsdk.device.ConnectableDevice
import com.example.remoteandroid.R
import com.example.remoteandroid.domain.models.ConnectionState
import com.example.remoteandroid.domain.models.DiscoveryState
import com.example.remoteandroid.screens.remote.models.DeviceUi
import com.example.remoteandroid.screens.remote.models.RemotePayload
import com.example.remoteandroid.screens.remote.models.RemoteViewState
import com.example.remoteandroid.screens.remote.models.TestConnectDeviceUiData
import com.example.remoteandroid.screens.remote.uidata.TestFindingDeviceUiData
import com.example.remoteandroid.screens.remote.uidata.TouchPadUiData
import com.example.remoteandroid.services.ResourceProvider
import com.example.remoteandroid.ui.recycler.UiData
import com.example.remoteandroid.ui.recycler.addUiItem
import com.example.remoteandroid.ui.recycler.addUiItems
import com.example.remoteandroid.ui.recycler.createUiDataList
import javax.inject.Inject

class RemoteContentUiMapper @Inject constructor(
    private val resourceProvider: ResourceProvider,
) {

    fun toContent(payload: RemotePayload): RemoteViewState {
        return RemoteViewState(
            content = createUiDataList {
                addUiItems { mapToFindDeviceUiData(payload) }
                addUiItem { mapToConnectDeviceUiData(payload) }
            }
        )
    }

    private fun mapToFindDeviceUiData(payload: RemotePayload): List<UiData> {
        return when (val discoveryState = payload.discoveryState) {
            is DiscoveryState.Failed -> listOf(TestFindingDeviceUiData(title = resourceProvider.getString(R.string.discovery_failed)))
            DiscoveryState.Searching -> listOf(TestFindingDeviceUiData(title = resourceProvider.getString(R.string.discovery_searching)))
            is DiscoveryState.Updated -> discoveryState.devices.map {
                TestFindingDeviceUiData(
                    title = resourceProvider.getString(
                        R.string.discovery_updated,
                        getConnectedDeviceTitle(discoveryState.devices)
                    ),
                    device = it
                )
            }

            DiscoveryState.NetworkError -> listOf(TestFindingDeviceUiData(title = resourceProvider.getString(R.string.discovery_network_error)))
        }
    }

    private fun mapToConnectDeviceUiData(payload: RemotePayload): UiData {
        val resource: String = when (val connectionState = payload.connectionState) {
            is ConnectionState.Connected -> resourceProvider.getString(
                R.string.connection_connected,
                getConnectedDeviceTitle(connectionState.device)
            )

            ConnectionState.Disconnected -> resourceProvider.getString(R.string.connection_disconnected)
            ConnectionState.Failed -> resourceProvider.getString(R.string.connection_failed)
            is ConnectionState.PairingRequired -> resourceProvider.getString(R.string.connection_pairing_required)
            ConnectionState.Waiting -> resourceProvider.getString(R.string.connection_waiting)
            ConnectionState.NetworkError -> resourceProvider.getString(R.string.discovery_network_error)
        }

        return TestConnectDeviceUiData(
            title = resource
        )
    }

    private fun getConnectedDeviceTitle(device: ConnectableDevice): String =
        "friendlyName = ${device.friendlyName}, modelName = ${device.modelName}"

    private fun getConnectedDeviceTitle(devices: List<ConnectableDevice>): String =
        devices.map { DeviceUi(it.friendlyName, it.modelName) }.joinToString(separator = "\n")
}
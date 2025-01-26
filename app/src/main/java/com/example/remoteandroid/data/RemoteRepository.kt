package com.example.remoteandroid.data

import com.connectsdk.device.ConnectableDevice
import com.connectsdk.device.ConnectableDeviceListener
import com.connectsdk.service.DeviceService
import com.connectsdk.service.command.ServiceCommandError
import com.example.remoteandroid.domain.models.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RemoteRepository {

    fun connectToDevice(connectableDevice: ConnectableDevice): Flow<ConnectionState> =
        callbackFlow {
            val deviceListener = object : ConnectableDeviceListener {
                override fun onDeviceReady(device: ConnectableDevice?) {
                    trySend(ConnectionState.Connected(connectableDevice))
                }

                override fun onDeviceDisconnected(device: ConnectableDevice?) {
                    trySend(ConnectionState.Disconnected)
                }

                override fun onPairingRequired(
                    device: ConnectableDevice?,
                    service: DeviceService?,
                    pairingType: DeviceService.PairingType?,
                ) {
                    pairingType?.let { trySend(ConnectionState.PairingRequired(it)) }
                }

                override fun onConnectionFailed(
                    device: ConnectableDevice?,
                    error: ServiceCommandError?,
                ) {
                    trySend(ConnectionState.Disconnected)
                }

                override fun onCapabilityUpdated(
                    device: ConnectableDevice?,
                    added: MutableList<String>?,
                    removed: MutableList<String>?,
                ) = Unit
            }
            connectableDevice.apply {
                addListener(deviceListener)
                setPairingType(null)
                connect()
            }
        }
}
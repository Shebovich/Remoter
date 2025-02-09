package com.example.remoteandroid.data

import com.connectsdk.device.ConnectableDevice
import com.connectsdk.device.ConnectableDeviceListener
import com.connectsdk.service.DeviceService
import com.connectsdk.service.command.ServiceCommandError
import com.example.remoteandroid.domain.models.ConnectionState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.first

class RemoteRepository {

    fun connectToDevice(connectableDevice: ConnectableDevice): Flow<ConnectionState> =
        callbackFlow {
            trySend(ConnectionState.Waiting)
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

            awaitClose {
                connectableDevice.removeListener(deviceListener)
                channel.close()
            }
        }


    fun enterPin(selectedDevice: ConnectableDevice, code: String) {
        selectedDevice.sendPairingKey(code)
    }
}
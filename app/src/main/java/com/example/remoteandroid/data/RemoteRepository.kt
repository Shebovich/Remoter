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

    private val _connectionState: MutableSharedFlow<ConnectionState> = MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val connectionState = _connectionState.asSharedFlow()

    suspend fun connectToDevice(connectableDevice: ConnectableDevice) {
            val deviceListener = object : ConnectableDeviceListener {
                override fun onDeviceReady(device: ConnectableDevice?) {
                    _connectionState.tryEmit(ConnectionState.Connected(connectableDevice))
                }

                override fun onDeviceDisconnected(device: ConnectableDevice?) {
                    _connectionState.tryEmit(ConnectionState.Disconnected)
                }

                override fun onPairingRequired(
                    device: ConnectableDevice?,
                    service: DeviceService?,
                    pairingType: DeviceService.PairingType?,
                ) {
                    pairingType?.let {
                        _connectionState.tryEmit(ConnectionState.PairingRequired(it))
                    }
                }

                override fun onConnectionFailed(
                    device: ConnectableDevice?,
                    error: ServiceCommandError?,
                ) {
                    _connectionState.tryEmit(ConnectionState.Disconnected)
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

    fun enterPin(selectedDevice: ConnectableDevice, code: String) {
        selectedDevice.sendPairingKey(code)
    }
}
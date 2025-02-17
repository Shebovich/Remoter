package com.example.remoteandroid.data

import com.connectsdk.device.ConnectableDevice
import com.connectsdk.device.ConnectableDeviceListener
import com.connectsdk.service.DeviceService
import com.connectsdk.service.DeviceService.PairingType
import com.connectsdk.service.capability.KeyControl
import com.connectsdk.service.capability.Launcher
import com.connectsdk.service.capability.MouseControl
import com.connectsdk.service.capability.PowerControl
import com.connectsdk.service.capability.TVControl
import com.connectsdk.service.capability.VolumeControl
import com.connectsdk.service.command.ServiceCommandError
import com.example.remoteandroid.domain.models.ConnectionState
import kotlinx.coroutines.Dispatchers
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
import kotlinx.coroutines.flow.flowOn

class RemoteRepository {

    fun connectToDevice(connectableDevice: ConnectableDevice): Flow<ConnectionState> =
        callbackFlow {
            val deviceListener = object : ConnectableDeviceListener {
                override fun onDeviceReady(device: ConnectableDevice?) {
                    device?.let {
                        println("services : ${device.services}")
                        trySend(
                            ConnectionState.Connected(
                                device = device,
                                powerControl = device.getCapability(PowerControl::class.java),
                                volumeControl = device.getCapability(VolumeControl::class.java),
                                tvControl = device.getCapability(TVControl::class.java),
                                launcher = device.getCapability(Launcher::class.java),
                                keyControl = device.getCapability(KeyControl::class.java),
                                mouseControl = device.getCapability(MouseControl::class.java)
                            )
                        )
                    }
                }

                override fun onDeviceDisconnected(device: ConnectableDevice?) {
                    trySend(ConnectionState.Disconnected)
                }

                override fun onPairingRequired(
                    device: ConnectableDevice?,
                    service: DeviceService?,
                    pairingType: PairingType?,
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
                ) {
                    println("onCapatabilityUpdated for device : $device and capatability : $added and removed $removed")
                }
            }
            connectableDevice.apply {
                addListener(deviceListener)
                setPairingType(PairingType.MIXED)
                connect()
            }

            awaitClose {
                connectableDevice.removeListener(deviceListener)
                channel.close()
            }
        }.flowOn(Dispatchers.Main)


    fun enterPin(selectedDevice: ConnectableDevice, code: String) {
        selectedDevice.sendPairingKey(code)
    }
}
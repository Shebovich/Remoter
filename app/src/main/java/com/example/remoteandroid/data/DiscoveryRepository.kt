package com.example.remoteandroid.data

import com.connectsdk.device.ConnectableDevice
import com.connectsdk.discovery.DiscoveryManager
import com.connectsdk.discovery.DiscoveryManagerListener
import com.connectsdk.service.command.ServiceCommandError
import com.example.remoteandroid.domain.models.DiscoveryState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.callbackFlow

class DiscoveryRepository {

    private val devices = mutableListOf<ConnectableDevice>()

    fun findDevice(): Flow<DiscoveryState> = callbackFlow {
        val listener = object : DiscoveryManagerListener {
        override fun onDeviceAdded(manager: DiscoveryManager?, device: ConnectableDevice?) {
            device?.let {
                devices.add(it)
                trySend(DiscoveryState.Updated(devices))
            }
        }

        override fun onDeviceUpdated(manager: DiscoveryManager?, device: ConnectableDevice?) {
            device?.let {
                devices.find { it.ipAddress == device.ipAddress }?.let {
                    devices.remove(it)
                    devices.add(it)
                    trySend(DiscoveryState.Updated(devices))
                }
            }
        }

        override fun onDeviceRemoved(manager: DiscoveryManager?, device: ConnectableDevice?) {
            device?.let {
                devices.find { it.ipAddress == device.ipAddress }?.let {
                    devices.remove(it)
                    devices.add(it)
                    trySend(DiscoveryState.Updated(devices))
                }
            }
        }

        override fun onDiscoveryFailed(
            manager: DiscoveryManager?,
            error: ServiceCommandError?,
        ) {
            println("Discovery failed")
            error?.let { trySend(DiscoveryState.Failed(it)) }
        }


    }
        DiscoveryManager.getInstance().addListener(listener)
        awaitClose {
            println("Closed")
            DiscoveryManager.getInstance().removeListener(listener)
            channel.close()
        }
    }
}
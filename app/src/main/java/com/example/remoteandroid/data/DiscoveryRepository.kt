package com.example.remoteandroid.data

import com.connectsdk.device.ConnectableDevice
import com.connectsdk.discovery.DiscoveryManager
import com.connectsdk.discovery.DiscoveryManagerListener
import com.connectsdk.service.command.ServiceCommandError
import com.example.remoteandroid.domain.models.DiscoveryState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class DiscoveryRepository {

    fun findDevice(): Flow<DiscoveryState> = callbackFlow {
        DiscoveryManager.getInstance().addListener(object : DiscoveryManagerListener {
            override fun onDeviceAdded(manager: DiscoveryManager?, device: ConnectableDevice?) {
                device?.let { trySend(DiscoveryState.Added(it)) }
            }

            override fun onDeviceUpdated(manager: DiscoveryManager?, device: ConnectableDevice?) {
                device?.let { trySend(DiscoveryState.Updated(it)) }
            }

            override fun onDeviceRemoved(manager: DiscoveryManager?, device: ConnectableDevice?) {
                device?.let { trySend(DiscoveryState.Removed(it)) }
            }

            override fun onDiscoveryFailed(
                manager: DiscoveryManager?,
                error: ServiceCommandError?,
            ) {
                error?.let { trySend(DiscoveryState.Failed(it)) }
            }

        })
    }
}
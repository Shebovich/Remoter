package com.example.remoteandroid.data

import com.connectsdk.core.AppInfo
import com.connectsdk.device.ConnectableDevice
import com.connectsdk.discovery.DiscoveryManager
import com.connectsdk.service.capability.Launcher
import com.connectsdk.service.capability.MouseControl
import com.connectsdk.service.command.ServiceCommandError
import com.connectsdk.service.sessions.LaunchSession
import com.example.remoteandroid.data.mappers.ApplicationsMapper
import com.example.remoteandroid.domain.models.ApplicationInfo
import com.example.remoteandroid.domain.models.ApplicationsState
import com.example.remoteandroid.domain.models.ConnectionState
import com.example.remoteandroid.domain.models.LaunchApplicationState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class ApplicationsRepository @Inject constructor(
    private val applicationsMapper: ApplicationsMapper,
) {

    fun getApplications(connectedState: ConnectionState.Connected): Flow<ApplicationsState> =
        callbackFlow {
            println(connectedState.device.services)
            connectedState.launcher?.getAppList(object : Launcher.AppListListener {
                override fun onError(error: ServiceCommandError?) {
                    println("getApplications : error ${error?.message}")
                    trySend(ApplicationsState.Error(error))
                }

                override fun onSuccess(data: MutableList<AppInfo>?) {
                    if (data == null) return
                    trySend(ApplicationsState.Success(filterData(data)))
                }

            })

            awaitClose {
                channel.close()
            }
        }

    private fun filterData(data: MutableList<AppInfo>): List<ApplicationInfo> {
        return data.filter { filterAppInfo(it) }.map { applicationsMapper.mapToApplicationInfo(it) }
    }

    private fun filterAppInfo(it: AppInfo): Boolean {
        val rawData = it.rawData
        if (rawData == null) return true
        val isSystemApp = rawData.optBoolean("systemApp")
        if (isSystemApp == false) {
            return true
        } else {
            return false
        }

    }

    fun launchApplication(
        connectableDevice: ConnectableDevice,
        id: String,
    ): Flow<LaunchApplicationState> =
        callbackFlow {
            val launcher = connectableDevice.getCapability(Launcher::class.java)
            launcher.launchApp(id, object : Launcher.AppLaunchListener {
                override fun onError(error: ServiceCommandError?) {
                    trySend(LaunchApplicationState.ERROR)
                }

                override fun onSuccess(`object`: LaunchSession?) {
                    trySend(LaunchApplicationState.SUCCESS)
                }
            })

            awaitClose {
                channel.close()
            }
        }
}
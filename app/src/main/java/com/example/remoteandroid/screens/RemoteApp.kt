package com.example.remoteandroid.screens

import android.app.Application
import com.connectsdk.discovery.DiscoveryManager
import com.connectsdk.service.DIALService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RemoteApp : Application() {

    override fun onCreate() {
        super.onCreate()
        DIALService.registerApp("Levak")
        DiscoveryManager.init(this)
    }
}
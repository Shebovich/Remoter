package com.example.remoteandroid.screens.main

import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.connectsdk.discovery.DiscoveryManager
import com.connectsdk.discovery.DiscoveryManager.PairingLevel
import com.example.remoteandroid.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val discoveryManager = DiscoveryManager.getInstance()
        discoveryManager.apply {
            registerDefaultDeviceTypes()
            pairingLevel = PairingLevel.ON
            start()
        }
        viewModel.onStart()
    }

    override fun onStop() {
        super.onStop()
        DiscoveryManager.getInstance().apply {
            stop()
        }
    }
}
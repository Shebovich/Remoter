package com.example.remoteandroid.screens.main

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.connectsdk.discovery.DiscoveryManager
import com.connectsdk.discovery.DiscoveryManager.PairingLevel
import com.example.remoteandroid.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private val viewModel: MainViewModel by viewModels()
    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            viewModel.onNetworkAvailable()
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            viewModel.onNetworkLost()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDiscoveryObserve()
        initNetworkObserver()
    }

    private fun initNetworkObserver() {
        val connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun initDiscoveryObserve() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.discoveryState.collect { discoveryState ->
                    println("Discovery state = $discoveryState")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initDiscoveryManager()
    }

    override fun onStop() {
        super.onStop()
        DiscoveryManager.getInstance().apply {
            stop()
        }
        val connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private fun initDiscoveryManager() {
        DiscoveryManager.getInstance().apply {
            registerDefaultDeviceTypes()
            pairingLevel = PairingLevel.ON
            start()
        }
    }

}
package com.example.remoteandroid.screens.remote

import com.connectsdk.device.ConnectableDevice
import com.example.remoteandroid.screens.remote.delegates.TestConnectDeviceDelegate
import com.example.remoteandroid.screens.remote.delegates.TestFindDeviceDelegate
import com.example.remoteandroid.screens.remote.delegates.TouchPadAdapterDelegate
import com.example.remoteandroid.screens.remote.models.MouseEvent
import com.example.remoteandroid.ui.recycler.RecyclerAdapterDelegated

class RemoteAdapter(
    onDeviceClicked: (ConnectableDevice?) -> Unit,
) : RecyclerAdapterDelegated(
    TestFindDeviceDelegate(onDeviceClicked),
    TestConnectDeviceDelegate(),
)
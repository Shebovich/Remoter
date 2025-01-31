package com.example.remoteandroid.screens.remote.delegates

import android.view.ViewGroup
import com.example.remoteandroid.R
import com.example.remoteandroid.databinding.TestConnectingDeviceLayoutBinding
import com.example.remoteandroid.screens.remote.models.TestConnectDeviceUiData
import com.example.remoteandroid.ui.recycler.AdapterDelegate
import com.example.remoteandroid.ui.recycler.BaseViewHolder
import com.example.remoteandroid.ui.recycler.UiData

class TestConnectDeviceDelegate : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder = ViewHolder(parent)

    override fun isValidForType(data: UiData): Boolean = data is TestConnectDeviceUiData

    inner class ViewHolder(parent: ViewGroup) :
        BaseViewHolder(parent, R.layout.test_connecting_device_layout) {

        private lateinit var binding: TestConnectingDeviceLayoutBinding
        override fun bind(data: UiData) {
            data as TestConnectDeviceUiData
            binding = TestConnectingDeviceLayoutBinding.bind(itemView)
            binding.testText.text = data.title
        }
    }
}
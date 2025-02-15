package com.example.remoteandroid.screens.remote.delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.connectsdk.device.ConnectableDevice
import com.example.remoteandroid.R
import com.example.remoteandroid.databinding.TestFindingDeviceLayoutBinding
import com.example.remoteandroid.screens.remote.uidata.TestFindingDeviceUiData
import com.example.remoteandroid.ui.recycler.AdapterDelegate
import com.example.remoteandroid.ui.recycler.BaseViewHolder
import com.example.remoteandroid.ui.recycler.UiData

class TestFindDeviceDelegate(
    private val onSearchingTVsClicked: () -> Unit
) : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder = ViewHolder(parent)

    override fun isValidForType(data: UiData): Boolean = data is TestFindingDeviceUiData

    inner class ViewHolder(parent: ViewGroup) : BaseViewHolder(
        parent,
        R.layout.test_finding_device_layout
    ) {

        private lateinit var binding: TestFindingDeviceLayoutBinding

        override fun bind(data: UiData) {
            data as TestFindingDeviceUiData
            binding = TestFindingDeviceLayoutBinding.bind(itemView)
            binding.testText.text = data.title
            itemView.setOnClickListener { onSearchingTVsClicked.invoke() }
        }

    }
}
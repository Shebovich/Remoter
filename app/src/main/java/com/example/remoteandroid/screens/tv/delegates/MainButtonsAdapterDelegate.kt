package com.example.remoteandroid.screens.tv.delegates

import android.view.ViewGroup
import com.example.remoteandroid.R
import com.example.remoteandroid.databinding.MainTvButtonsLayoutBinding
import com.example.remoteandroid.screens.remote.models.TestConnectDeviceUiData
import com.example.remoteandroid.screens.tv.models.ButtonId
import com.example.remoteandroid.screens.tv.models.onTvButtonClicked
import com.example.remoteandroid.screens.tv.uidata.MainButtonsUiData
import com.example.remoteandroid.ui.recycler.AdapterDelegate
import com.example.remoteandroid.ui.recycler.BaseViewHolder
import com.example.remoteandroid.ui.recycler.UiData

class MainButtonsAdapterDelegate(
    private val onButtonClicked: (ButtonId) -> Unit
) : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder = ViewHolder(parent)

    override fun isValidForType(data: UiData): Boolean = data is MainButtonsUiData

    inner class ViewHolder(parent: ViewGroup) :
        BaseViewHolder(parent, R.layout.main_tv_buttons_layout) {

        private lateinit var binding: MainTvButtonsLayoutBinding
        override fun bind(data: UiData) {
            data as TestConnectDeviceUiData
            binding = MainTvButtonsLayoutBinding.bind(itemView)
            binding.powerButton.onTvButtonClicked(ButtonId.OFF, onButtonClicked)
            binding.searchButton.onTvButtonClicked(ButtonId.SEARCH, onButtonClicked)
            binding.browserButton.onTvButtonClicked(ButtonId.BROWSER, onButtonClicked)
            binding.sourceButton.onTvButtonClicked(ButtonId.EXIT, onButtonClicked)
        }
    }
}
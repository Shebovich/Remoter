package com.example.remoteandroid.screens.tv.delegates

import android.view.ViewGroup
import com.example.remoteandroid.R
import com.example.remoteandroid.databinding.MainTvButtonsLayoutBinding
import com.example.remoteandroid.databinding.SecondaryButtonsTvLayoutBinding
import com.example.remoteandroid.databinding.TestConnectingDeviceLayoutBinding
import com.example.remoteandroid.screens.remote.models.TestConnectDeviceUiData
import com.example.remoteandroid.screens.tv.models.ButtonId
import com.example.remoteandroid.screens.tv.models.onTvButtonClicked
import com.example.remoteandroid.screens.tv.uidata.MainButtonsUiData
import com.example.remoteandroid.screens.tv.uidata.SecondaryButtonsUiData
import com.example.remoteandroid.ui.recycler.AdapterDelegate
import com.example.remoteandroid.ui.recycler.BaseViewHolder
import com.example.remoteandroid.ui.recycler.UiData

class SecondaryButtonsAdapterDelegate(
    private val onButtonClicked: (ButtonId) -> Unit
) : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder = ViewHolder(parent)

    override fun isValidForType(data: UiData): Boolean = data is SecondaryButtonsUiData

    inner class ViewHolder(parent: ViewGroup) :
        BaseViewHolder(parent, R.layout.secondary_buttons_tv_layout) {

        private lateinit var binding: SecondaryButtonsTvLayoutBinding
        override fun bind(data: UiData) {
            binding = SecondaryButtonsTvLayoutBinding.bind(itemView)
            binding.apply {
                volumeUpButton.onTvButtonClicked(ButtonId.VOLUME_UP, onButtonClicked)
                volumeDownButton.onTvButtonClicked(ButtonId.VOLUME_DOWN, onButtonClicked)
                homeButton.onTvButtonClicked(ButtonId.HOME, onButtonClicked)
                channelUpButton.onTvButtonClicked(ButtonId.CHANNEL_UP, onButtonClicked)
                channelDownButton.onTvButtonClicked(ButtonId.CHANNEL_DOWN, onButtonClicked)
                channelListButton.onTvButtonClicked(ButtonId.LIST_CHANNELS, onButtonClicked)
                muteButton.onTvButtonClicked(ButtonId.MUTE, onButtonClicked)
                channelListButtonSecond.onTvButtonClicked(ButtonId.CHANNEL_ONE, onButtonClicked)
            }
        }
    }
}
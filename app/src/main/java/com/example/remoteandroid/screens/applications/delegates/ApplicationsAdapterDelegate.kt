package com.example.remoteandroid.screens.applications.delegates

import android.view.ViewGroup
import com.example.remoteandroid.R
import com.example.remoteandroid.databinding.ApplicationItemLayoutBinding
import com.example.remoteandroid.domain.models.ApplicationInfo
import com.example.remoteandroid.screens.applications.uidata.ApplicationUiData
import com.example.remoteandroid.ui.recycler.AdapterDelegate
import com.example.remoteandroid.ui.recycler.BaseViewHolder
import com.example.remoteandroid.ui.recycler.UiData

class ApplicationsAdapterDelegate(
    private val onApplicationClicked: (ApplicationInfo) -> Unit
) : AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder = ViewHolder(parent)

    override fun isValidForType(data: UiData): Boolean = data is ApplicationUiData

    inner class ViewHolder(parent: ViewGroup) :
        BaseViewHolder(parent, R.layout.application_item_layout) {

        private lateinit var binding: ApplicationItemLayoutBinding
        override fun bind(data: UiData) {
            val data = data as ApplicationUiData
            binding = ApplicationItemLayoutBinding.bind(itemView)
            binding.title.text = data.applicationInfo.name
            binding.root.setOnClickListener {
                onApplicationClicked.invoke(data.applicationInfo)
            }
        }
    }
}
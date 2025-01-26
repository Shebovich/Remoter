package com.example.remoteandroid.ui.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class RecyclerAdapterDelegated(
    vararg delegates: AdapterDelegate,
) : RecyclerView.Adapter<BaseViewHolder>() {

    private val delegateManager = AdapterDelegateManager()

    init {
        delegates.forEach(delegateManager::addDelegate)
    }

    fun submitData(data: List<UiData>) {
        delegateManager.submitData(
            data = data,
            adapter = this
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return delegateManager.getDelegate(viewType).onCreateViewHolder(parent)
    }

    override fun getItemCount(): Int = delegateManager.getItems().size

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        delegateManager.bindViewHolder(
            holder = holder,
            position = position
        )
    }

    override fun getItemViewType(position: Int): Int {
        return delegateManager.getItemViewType(position)
    }
}
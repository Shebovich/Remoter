package com.example.remoteandroid.ui.recycler

import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class AdapterDelegateManager {

    private val delegates = SparseArrayCompat<AdapterDelegate>()
    private val uiItems = mutableListOf<UiData>()

    fun addDelegate(delegate: AdapterDelegate) {
        delegates.put(delegates.size(), delegate)
    }

    fun getDelegate(viewType: Int): AdapterDelegate = delegates[viewType]
        ?: throw NullPointerException("Adapter delegates for viewType $viewType was not found")

    fun getItemViewType(position: Int): Int {
        val uiItem = uiItems.getOrNull(position)
            ?: throw NullPointerException("UiItem for position $position was not found")

        for (i in 0 until delegates.size()) {
            if (delegates.get(i)?.isValidForType(uiItem) == true) {
                return delegates.keyAt(i)
            }
        }

        throw NullPointerException("Adapter delegates for position $position was not found")
    }

    fun getItems() : List<UiData> = uiItems

    fun bindViewHolder(holder: BaseViewHolder, position: Int) =
        uiItems.getOrNull(position)?.run(holder::bind)

    fun attachViewHolder(holder: BaseViewHolder) {
        holder.onAttached()
    }

    fun detachViewHolder(holder: BaseViewHolder) {
        holder.onDetached()
    }

    fun submitData(data: List<UiData>, adapter: RecyclerView.Adapter<BaseViewHolder>) {
        val currentItems = uiItems.toList()

        uiItems.clear()
        uiItems.addAll(data)

        DiffUtil.calculateDiff(DiffCallback(currentItems, uiItems), true)
            .dispatchUpdatesTo(adapter)
    }
}
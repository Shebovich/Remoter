package com.example.remoteandroid.ui.recycler

import androidx.recyclerview.widget.DiffUtil

class DiffCallback(
    private val old: List<UiData>,
    private val new: List<UiData>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        old[oldItemPosition] == new[newItemPosition]

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = old[oldItemPosition]
        val newItem = new[newItemPosition]
        val oldId = oldItem.getDiffId()
        val newId = newItem.getDiffId()
        return when {
            oldId != null && newId != null -> oldId == newId
            else -> oldItem == newItem
        }
    }
}
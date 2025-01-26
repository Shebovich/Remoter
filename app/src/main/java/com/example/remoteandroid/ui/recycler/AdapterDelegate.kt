package com.example.remoteandroid.ui.recycler

import android.view.ViewGroup

interface AdapterDelegate {

    fun onCreateViewHolder(parent: ViewGroup): BaseViewHolder
    fun isValidForType(data: UiData): Boolean
}
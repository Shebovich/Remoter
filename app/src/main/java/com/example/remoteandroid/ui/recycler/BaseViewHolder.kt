package com.example.remoteandroid.ui.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder : RecyclerView.ViewHolder {

    constructor(
        parent: ViewGroup,
        layoutId: Int
    ) : super(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    constructor(view: View): super(view)

    abstract fun bind(data: UiData)

    open fun onAttached() {
        // nothing by default
    }

    open fun onDetached() {
        // nothing by default
    }
}
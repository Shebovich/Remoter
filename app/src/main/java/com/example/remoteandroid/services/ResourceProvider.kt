package com.example.remoteandroid.services

import android.content.Context

class ResourceProvider(
    private val context: Context
) {

    fun getString(resource: Int) : String =
        context.resources.getString(resource)
}
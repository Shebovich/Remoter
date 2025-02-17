package com.example.remoteandroid.screens.applications.mappers

import com.example.remoteandroid.domain.models.ApplicationsState
import com.example.remoteandroid.screens.applications.models.ApplicationsContentViewState
import com.example.remoteandroid.screens.applications.models.ApplicationsPayload
import com.example.remoteandroid.screens.applications.uidata.ApplicationUiData
import com.example.remoteandroid.ui.recycler.addUiItems
import com.example.remoteandroid.ui.recycler.createUiDataList

class ApplicationsContentUiMapper {

    fun toContent(payload: ApplicationsPayload) = ApplicationsContentViewState(
        content = createUiDataList {
            when (val appState = payload.state) {
                is ApplicationsState.Error -> {

                }

                ApplicationsState.Loading -> {

                }

                is ApplicationsState.Success -> {
                    addUiItems {
                        appState.data.map {
                            ApplicationUiData(it)
                        }
                    }
                }
            }
        }
    )
}
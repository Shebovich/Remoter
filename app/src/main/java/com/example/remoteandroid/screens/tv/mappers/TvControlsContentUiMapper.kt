package com.example.remoteandroid.screens.tv.mappers

import com.example.remoteandroid.screens.remote.uidata.TouchPadUiData
import com.example.remoteandroid.screens.tv.models.TvControlsPayload
import com.example.remoteandroid.screens.tv.models.TvControlsViewState
import com.example.remoteandroid.screens.tv.uidata.MainButtonsUiData
import com.example.remoteandroid.screens.tv.uidata.SecondaryButtonsUiData
import com.example.remoteandroid.ui.recycler.addUiItem
import com.example.remoteandroid.ui.recycler.createUiDataList

class TvControlsContentUiMapper() {

    fun toContent(payload: TvControlsPayload) : TvControlsViewState {
        return TvControlsViewState(
            content = createUiDataList {
                addUiItem { MainButtonsUiData() }
                addUiItem { SecondaryButtonsUiData() }
                addUiItem { TouchPadUiData() }
            }
        )
    }
}
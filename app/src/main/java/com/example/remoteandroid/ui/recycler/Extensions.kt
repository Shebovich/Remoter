package com.example.remoteandroid.ui.recycler

fun createUiDataList(block: MutableList<UiData>.() -> Unit): List<UiData> {
    val list = mutableListOf<UiData>()
    block.invoke(list)
    return list
}

fun MutableList<UiData>.addUiItem(block: () -> UiData) {
    add(block())
}

fun MutableList<UiData>.addUiItems(block: () -> List<UiData>) {
    addAll(block())
}
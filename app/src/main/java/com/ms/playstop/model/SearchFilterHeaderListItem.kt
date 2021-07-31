package com.ms.playstop.model

import com.ms.playstop.ui.search.filter.adapter.FilterItem

data class SearchFilterHeaderListItem(
    val name: String = "",
    val children: ArrayList<FilterItem> = arrayListOf(),
    var selectedPositions: ArrayList<FilterItem> = arrayListOf(),
    val hasMultipleChoices: Boolean = true,
    override var isSelected: Boolean = false
): FilterItem {
    override fun getFilterName(): String {
        return name
    }

    override fun getFilterId(): String {
        return ""
    }
}
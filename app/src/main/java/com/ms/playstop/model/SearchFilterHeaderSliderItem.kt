package com.ms.playstop.model

import com.ms.playstop.ui.search.filter.adapter.FilterItem

data class SearchFilterHeaderSliderItem(
    val name: String = "",
    var baseValues: Pair<Float,Float> = 0f to 10f,
    var selectedValues: Pair<Float,Float> = 0f to 10f,
    override var isSelected: Boolean = false
): FilterItem {
    override fun getFilterName(): String {
        return name
    }

    override fun getFilterId(): String {
        return ""
    }
}
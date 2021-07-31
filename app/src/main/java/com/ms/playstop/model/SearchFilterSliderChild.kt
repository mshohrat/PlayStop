package com.ms.playstop.model

import com.ms.playstop.ui.search.filter.adapter.FilterItem

data class SearchFilterSliderChild(
    val baseValues: Pair<Float,Float> = 0f to 10f,
    var selectedValues: Pair<Float,Float> = 0f to 10f,
    override var isSelected: Boolean = false
): FilterItem {
    override fun getFilterName(): String {
        return ""
    }

    override fun getFilterId(): String {
        return ""
    }
}
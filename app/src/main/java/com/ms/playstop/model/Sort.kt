package com.ms.playstop.model

import com.ms.playstop.ui.search.filter.adapter.FilterItem

data class Sort(
    val type: Int,
    val name: String,
    override var isSelected: Boolean = false
) : FilterItem {
    override fun getFilterName(): String {
        return name
    }

    override fun getFilterId(): String {
        return type.toString()
    }
}
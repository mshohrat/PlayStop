package com.ms.playstop.model

import android.os.Parcelable
import com.ms.playstop.ui.search.filter.adapter.FilterItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Year(
    val value: Int = 0,
    override var isSelected: Boolean = false
) : Parcelable,FilterItem {
    override fun getFilterName(): String {
        return value.toString()
    }

    override fun getFilterId(): String {
        return value.toString()
    }
}
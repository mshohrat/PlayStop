package com.ms.playstop.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.ms.playstop.ui.search.filter.adapter.FilterItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Genre(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    override var isSelected: Boolean = false
) : FilterItem, Parcelable {
    override fun getFilterName(): String {
        return name
    }

    override fun getFilterId(): String {
        return id.toString()
    }
}
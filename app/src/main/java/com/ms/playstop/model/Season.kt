package com.ms.playstop.model

import com.google.gson.annotations.SerializedName

data class Season(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("trailer")
    val trailer: String?,
    @SerializedName("episodes")
    val episodes: List<Episode>?
)
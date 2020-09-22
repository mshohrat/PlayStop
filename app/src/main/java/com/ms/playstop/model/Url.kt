package com.ms.playstop.model

import com.google.gson.annotations.SerializedName

data class Url(
    @SerializedName("id")
    val id: Int,
    @SerializedName("link")
    val link: String,
    @SerializedName("volume")
    val volume: Int = 0,
    @SerializedName("quality")
    val quality: Quality
)
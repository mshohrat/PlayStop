package com.ms.playstop.model

import com.google.gson.annotations.SerializedName

data class Subtitle(
    @SerializedName("id")
    val id: Int,
    @SerializedName("link")
    val link: String?
)
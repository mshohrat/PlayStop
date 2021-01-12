package com.ms.playstop.model

import com.google.gson.annotations.SerializedName

data class Episode(
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
    @SerializedName("length")
    val length: Int,
    @SerializedName("price")
    val price: Float,
    @SerializedName("urls")
    val urls: List<Url>?,
    @SerializedName("subtitles")
    val subtitles: List<Subtitle>?
)
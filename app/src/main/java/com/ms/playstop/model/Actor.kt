package com.ms.playstop.model

import com.google.gson.annotations.SerializedName

data class Actor(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
package com.ms.playstop.model

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user")
    val user: User,
    @SerializedName("text")
    val text: String?
)
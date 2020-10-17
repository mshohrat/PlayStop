package com.ms.playstop.model

import com.google.gson.annotations.SerializedName

data class UpdateApp(
    @SerializedName("min_supported_version")
    val minVersion: Int,
    @SerializedName("last_version")
    val lastVersion: Int
)

package com.ms.playstop.model

import com.google.gson.annotations.SerializedName

data class Device(
    @SerializedName("id")
    val id: Int,
    @SerializedName("model")
    val model: String?,
    @SerializedName("platform")
    val platform: String?,
    @SerializedName("platform_version")
    val platformVersion: Int?,
    @SerializedName("version")
    val appVersion: String?,
    @SerializedName("last_usage_date")
    val lastUsageDate: String?
)
package com.ms.playstop.model

import com.google.gson.annotations.SerializedName

data class Features(
    @SerializedName("is_subscription_enabled")
    val isSubscriptionEnabled: Boolean = false
)
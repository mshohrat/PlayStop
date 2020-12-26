package com.ms.playstop.network.model

import com.google.gson.annotations.SerializedName
import com.ms.playstop.BuildConfig

data class RefreshTokenRequest(
    @SerializedName("refresh_token")
    val refreshToken: String? = null,
    @SerializedName("grant_type")
    val grantType: String = "refresh_token",
    @SerializedName("client_id")
    val clientId: String = BuildConfig.CLIENT_ID,
    @SerializedName("client_secret")
    val clientSecret: String = BuildConfig.CLIENT_SECRET
)

data class RegisterFbTokenRequest(
    @SerializedName("token")
    val token: String?
)

data class PostCommentRequest (
    @SerializedName("text")
    val text: String?
)

data class SignupRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class LoginRequest(
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("password")
    val password: String? = null,
    @SerializedName("grant_type")
    val grantType : String = "password",
    @SerializedName("client_id")
    val clientId: String = BuildConfig.CLIENT_ID,
    @SerializedName("client_secret")
    val clientSecret: String = BuildConfig.CLIENT_SECRET
)

data class GetAllSuggestionsMoviesRequest(
    @SerializedName("suggestions")
    val suggestions: List<Int>
)
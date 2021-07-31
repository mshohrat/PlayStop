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

data class UpdateAccountRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String?
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

data class LoginWithPhoneNumberRequest(
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("otp")
    val code: String? = null,
    @SerializedName("grant_type")
    val grantType : String = "otp",
    @SerializedName("client_id")
    val clientId: String = BuildConfig.CLIENT_ID,
    @SerializedName("client_secret")
    val clientSecret: String = BuildConfig.CLIENT_SECRET
)

data class PhoneNumberRequest(
    @SerializedName("phone")
    val phone: String? = null
)

data class VerifyPhoneNumberRequest(
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("code")
    val code: String? = null
)

data class GetAllSuggestionsMoviesRequest(
    @SerializedName("suggestions")
    val suggestions: List<Int>
)

data class ResetPasswordRequest(
    @SerializedName("email")
    val email: String
)

data class VerifySignupOtpRequest(
    @SerializedName("phone")
    val phone: String,
    @SerializedName("code")
    val code: String
)

data class VerifyPaymentRequest(
    @SerializedName("transaction_id")
    val transactionId: String
)

data class SearchMovieRequest(
    @SerializedName("query")
    val query: String?,
    @SerializedName("sort")
    val sort: String? = null,
    @SerializedName("categories")
    val categories: List<Int>? = null,
    @SerializedName("genres")
    val genres: List<Int>? = null,
    @SerializedName("languages")
    val languages: List<Int>? = null,
    @SerializedName("years")
    val years: List<Int>? = null,
    @SerializedName("countries")
    val countries: List<Int>? = null,
    @SerializedName("minimum_score")
    val minimumScore: Float? = null,
    @SerializedName("maximum_score")
    val maximumScore: Float? = null
)
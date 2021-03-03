package com.ms.playstop.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("score")
    val score: Float?,
    @SerializedName("score_votes")
    val scoreVotes: Int?,
    @SerializedName("production_year")
    val productionYear: Int,
    @SerializedName("length")
    val length: Int,
    @SerializedName("price")
    val price: Float,
    @SerializedName("trailer")
    val trailer: String?,
    @SerializedName("image")
    val image: String,
    @SerializedName("header")
    val header: String?,
    @SerializedName("is_series")
    val isSeries: Boolean,
    @SerializedName("category")
    val category: Category?,
    @SerializedName("director")
    val director: String?,
    @SerializedName("writer")
    val writer: String?,
    @SerializedName("genres")
    val genres: List<Genre>?,
    @SerializedName("languages")
    val languages: List<Language>?,
    @SerializedName("countries")
    val countries: List<Country>?,
    @SerializedName("actors")
    val actors: String?,
    @SerializedName("seasons")
    val seasons: List<Season>?,
    @SerializedName("urls")
    val urls: List<Url>?,
    @SerializedName("comments")
    val comments: List<Comment>?,
    @SerializedName("subtitles")
    val subtitles: List<Subtitle>?,
    @SerializedName("subtitle")
    var subtitle: String?,
    @SerializedName("is_liked")
    var isLiked: Boolean = false
)
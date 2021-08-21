package com.ms.playstop.model

import com.google.gson.annotations.SerializedName

data class GenresSuggestion(
    @SerializedName("genre_ids")
    val genreIds : List<Int>,
    @SerializedName("name")
    val name: String
)
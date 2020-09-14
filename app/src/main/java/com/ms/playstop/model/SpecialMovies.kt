package com.ms.playstop.model

import com.google.gson.annotations.SerializedName

data class SpecialMovies(
    @SerializedName("name")
    val name: String,
    @SerializedName("movies")
    val movies: ArrayList<Movie>
)
package com.ms.playstop.model

import com.google.gson.annotations.SerializedName

data class SuggestionMovies(
    @SerializedName("suggestion")
    val suggestion: Suggestion,
    @SerializedName("movies")
    val movies: List<Movie>
) : MovieListItem()
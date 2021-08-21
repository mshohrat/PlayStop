package com.ms.playstop.model

import com.google.gson.annotations.SerializedName

data class GenresSuggestionMovies(
    @SerializedName("genres_suggestion")
    val genresSuggestion : GenresSuggestion,
    @SerializedName("movies")
    val movies: List<Movie>
) : MovieListItem()
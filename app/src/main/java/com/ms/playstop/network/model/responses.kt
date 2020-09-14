package com.ms.playstop.network.model

import com.google.gson.annotations.SerializedName
import com.ms.playstop.model.*

data class RefreshTokenResponse(
    @SerializedName("access_token")
    val token: String? = null,
    @SerializedName("refresh_token")
    val refreshToken: String? = null,
    @SerializedName("expires_in")
    val expiresIn: Long = 3000000
)

data class ConfigResponse(
    @SerializedName("suggestions")
    val suggestions: List<Suggestion>?,
    @SerializedName("categories")
    val categories: List<Category>?,
    @SerializedName("genres")
    val genres: List<Genre>?,
    @SerializedName("specials")
    val specialsName: String?,
    @SerializedName("is_user_active")
    val isUserActive: Boolean?,
    @SerializedName("requires_token")
    val requiresToken: Boolean?,
    @SerializedName("user")
    val user: UserResponse?
) {
    companion object {
        const val SAVE_KEY = "Config Response Save Key"
    }
}

data class MovieListResponse(
    @SerializedName("movies")
    val movies : List<Movie>?
)

data class MoviePagedListResponse(
    @SerializedName("movies")
    val movies : List<Movie>?,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)

data class CommentListResponse(
    @SerializedName("comments")
    val comments : List<Comment?>?,
    @SerializedName("current_page")
    val currentPage: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)

data class MovieResponse(
    @SerializedName("movie")
    val movie: Movie?
)

data class SeasonResponse(
    @SerializedName("season")
    val season: Season?
)

data class EpisodeResponse(
    @SerializedName("episode")
    val episode: Episode?
)

data class GeneralResponse(
    @SerializedName("message")
    val message: String? = null,
    val messageResId: Int? = null
)

data class CategoryListResponse(
    @SerializedName("categories")
    val categories : List<Category?>?
)

data class CountryListResponse(
    @SerializedName("countries")
    val countries : List<Country?>?
)

data class GenreListResponse(
    @SerializedName("genres")
    val genres : List<Genre?>?
)

data class LanguageListResponse(
    @SerializedName("languages")
    val languages : List<Language?>?
)

data class ActorListResponse(
    @SerializedName("actors")
    val actors : List<Actor?>?
)

data class WriterListResponse(
    @SerializedName("writes")
    val writers : List<Writer?>?
)

data class DirectorListResponse(
    @SerializedName("directors")
    val directors : List<Director?>?
)

data class LoginResponse(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("access_token")
    val token: String? = null,
    @SerializedName("refresh_token")
    val refreshToken: String? = null,
    @SerializedName("is_user_active")
    val isUserActive: Boolean,
    @SerializedName("expires_in")
    val expiresIn: Long = 3000000
)

data class AllSuggestionsMoviesResponse(
    @SerializedName("suggestion_movies")
    val suggestionMovies : ArrayList<SuggestionMovies>?,
    @SerializedName("special_movies")
    val specialMovies: SpecialMovies?
)

data class InvalidCredentialsResponse(
    @SerializedName("message")
    val message: String?,
    @SerializedName("errors")
    val invalidCredentialsData: InvalidCredentialsData?
)

data class InvalidCredentialsData(
    @SerializedName("email")
    val emailErrors : List<String?>?,
    @SerializedName("password")
    val passwordErrors : List<String?>?,
    @SerializedName("name")
    val nameErrors : List<String?>?
)

data class UserResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String
)
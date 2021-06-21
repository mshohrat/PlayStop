package com.ms.playstop.network.model

import com.google.gson.annotations.SerializedName
import com.ms.playstop.model.*
import kotlin.collections.ArrayList

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
    @SerializedName("minimum_movie_year")
    val minimumMovieYear: Int = 0,
    @SerializedName("maximum_movie_year")
    val maximumMovieYear: Int = 0,
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
    val user: UserInfoResponse?,
    @SerializedName("update_app")
    val updateApp: UpdateApp?
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
    val actors : List<Character?>?
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
    val isUserActive: Boolean = true,
    @SerializedName("expires_in")
    val expiresIn: Long = 3000000,
    @SerializedName("phone_verified")
    val isPhoneVerified: Boolean = false,
    @SerializedName("email_verified")
    val isEmailVerified: Boolean = false,
    @SerializedName("phone")
    val phone: String? = null,
    @SerializedName("is_new_user")
    val isNewUser: Boolean = false
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
    val nameErrors : List<String?>?,
    @SerializedName("phone")
    val phoneErrors : List<String?>?
)

data class UserInfoResponse(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone_verified")
    val isPhoneVerified: Boolean = false,
    @SerializedName("phone")
    val phone: String?
)

data class UserResponse(
    @SerializedName("user")
    val user: UserInfoResponse?
)

data class EnterPhoneNumberResponse(
    @SerializedName("result")
    val result: EnterPhoneNumberResult?,
    @SerializedName("items")
    val items: ArrayList<Long?>?
)

data class EnterPhoneNumberResult(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("message")
    val message: String?
)

data class ActorInfoResponse(
    @SerializedName("actor")
    val actor: Character?
)

data class DirectorInfoResponse(
    @SerializedName("director")
    val director: Character?
)

data class WriterInfoResponse(
    @SerializedName("writer")
    val writer: Character?
)
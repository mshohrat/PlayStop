package com.ms.playstop.network.base

import com.ms.playstop.BuildConfig
import com.ms.playstop.network.model.*
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {

    @GET("config")
    fun getConfig() : Single<ConfigResponse?>?

    @POST("movies/suggestions")
    fun getAllSuggestionsMovies(
        @Body getAllSuggestionsMoviesRequest: GetAllSuggestionsMoviesRequest
    ) : Single<AllSuggestionsMoviesResponse?>?

    @GET("movies/{page}")
    fun getLastMovies(
        @Path("page") page : Int
    ) : Single<MoviePagedListResponse?>?

    @GET("movies/{page}/{suggestion_id}")
    fun getSuggestionMovies(
        @Path("page") page : Int
        ,@Path("suggestion_id") suggestionId : Int
    ) : Single<MoviePagedListResponse?>?

    @GET("movies/special/1/{page}")
    fun getSpecialMovies(
        @Path("page") page : Int
    ) : Single<MoviePagedListResponse?>?

    @GET("movies/director/{id}/{page}")
    fun getDirectorMovies(
        @Path("page") page : Int
        ,@Path("id") directorId : Int
    ) : Single<MoviePagedListResponse?>?

    @GET("movies/actor/{id}/{page}")
    fun getActorMovies(
        @Path("page") page : Int
        ,@Path("id") actorId : Int
    ) : Single<MoviePagedListResponse?>?

    @GET("movies/writer/{id}/{page}")
    fun getWriterMovies(
        @Path("page") page : Int
        ,@Path("id") writerId : Int
    ) : Single<MoviePagedListResponse?>?

    @GET("movies/category/{id}/{page}")
    fun getCategoryMovies(
        @Path("page") page : Int
        ,@Path("id") categoryId : Int
    ) : Single<MoviePagedListResponse?>?

    @GET("movies/quality/{id}/{page}")
    fun getQualityMovies(
        @Path("page") page : Int
        ,@Path("id") qualityId : Int
    ) : Single<MoviePagedListResponse?>?

    @GET("movies/language/{id}/{page}")
    fun getLanguageMovies(
        @Path("page") page : Int
        ,@Path("id") languageId : Int
    ) : Single<MoviePagedListResponse?>?

    @GET("movies/genre/{id}/{page}")
    fun getGenreMovies(
        @Path("page") page : Int
        ,@Path("id") genreId : Int
    ) : Single<MoviePagedListResponse?>?

    @GET("movies/year/{year}/{page}")
    fun getYearMovies(
        @Path("page") page : Int
        ,@Path("year") year : Int
    ) : Single<MoviePagedListResponse?>?

    @GET("movie/{id}")
    fun getMovie(
        @Path("id") movieId : Int
    ) : Single<MovieResponse?>?

    @GET("season/{id}")
    fun getSeason(
        @Path("id") seasonId : Int
    ) : Single<SeasonResponse?>?

    @GET("episode/{id}")
    fun getEpisode(
        @Path("id") episodeId : Int
    ) : Single<EpisodeResponse?>?

    @GET("comments/{movie_id}/{page}")
    fun getMovieComments(
        @Path("movie_id") movieId : Int,
        @Path("page") page : Int
    ) : Single<CommentListResponse?>?

    @POST("fb-token")
    fun registerFbToken(
        @Body registerFbTokenRequest: RegisterFbTokenRequest
    ) : Single<GeneralResponse?>?

    @GET("categories")
    fun getCategories() : Single<CategoryListResponse?>?

    @GET("countries")
    fun getCountries() : Single<CountryListResponse?>?

    @GET("genres")
    fun getGenres() : Single<GenreListResponse?>?

    @GET("languages")
    fun getLanguages() : Single<LanguageListResponse?>?

    @GET("directors/{page}")
    fun getDirectors(
        @Path("page") page: Int
    ) : Single<DirectorListResponse?>?

    @GET("writers/{page}")
    fun getWriters(
        @Path("page") page: Int
    ) : Single<WriterListResponse?>?

    @GET("actors/{page}")
    fun getActors(
        @Path("page") page: Int
    ) : Single<ActorListResponse?>?

    @GET("search/actor")
    fun searchActor(
        @Query("query") query: String?
    ) : Single<ActorListResponse?>?

    @GET("search/director")
    fun searchDirector(
        @Query("query") query: String?
    ) : Single<DirectorListResponse?>?

    @GET("search/writer")
    fun searchWriter(
        @Query("query") query: String?
    ) : Single<WriterListResponse?>?

    @GET("search/movie")
    fun searchMovie(
        @Query("query") query: String?
    ) : Single<MovieListResponse?>?

    @POST("comment/{movie_id}")
    fun postComment(
        @Body postCommentRequest: PostCommentRequest?,@Path("movie_id") movieId: Int
    ) : Single<GeneralResponse?>?

    @POST("register")
    fun signup(
        @Body signupRequest: SignupRequest?
    ) : Single<GenreListResponse?>?

    @POST("auth/login")
    fun login(
        @Body loginRequest: LoginRequest?
    ) : Single<LoginResponse?>?

    @POST()
    fun refreshToken(@Body request: RefreshTokenRequest, @Url url: String = BuildConfig.SERVER_ADDRESS + "oauth/token"): Single<RefreshTokenResponse?>?
}
package com.ms.playstop.network.base

import com.ms.playstop.BuildConfig
import com.ms.playstop.network.model.*
import io.reactivex.Single
import retrofit2.http.*

interface ApiService {

    @GET("v1/config")
    fun getConfig() : Single<ConfigResponse?>?

    @POST("v1/movies/suggestions")
    fun getAllSuggestionsMovies(
        @Body getAllSuggestionsMoviesRequest: GetAllSuggestionsMoviesRequest
    ) : Single<AllSuggestionsMoviesResponse?>?

    @GET("v1/suggestions/movies/{page}")
    fun getAllGenresSuggestionsMovies(
        @Path("page") page: Int
    ) : Single<GenresSuggestionMoviesResponse?>?

    @GET("v1/movies/{page}")
    fun getMovies(
        @Path("page") page : Int,
        @Query("sort") sort : String? = null
    ) : Single<MoviePagedListResponse?>?

    @GET("v1/movies/{page}/{suggestion_id}")
    fun getSuggestionMovies(
        @Path("page") page : Int,
        @Path("suggestion_id") suggestionId : Int,
        @Query("sort") sort : String? = null
    ) : Single<MoviePagedListResponse?>?

    @POST("v1/genres_suggestions/movies/{page}")
    fun getGenresSuggestionMovies(
        @Path("page") page : Int,
        @Body genresSuggestionMoviesRequest: GenresSuggestionMoviesRequest
    ) : Single<MoviePagedListResponse?>?

    @GET("v1/movies/special/1/{page}")
    fun getSpecialMovies(
        @Path("page") page : Int,
        @Query("sort") sort : String? = null
    ) : Single<MoviePagedListResponse?>?

    @GET("v1/directors/{id}/movies/{page}")
    fun getDirectorMovies(
        @Path("page") page : Int,
        @Path("id") directorId : Int,
        @Query("sort") sort : String? = null
    ) : Single<MoviePagedListResponse?>?

    @GET("v1/actors/{id}/movies/{page}")
    fun getActorMovies(
        @Path("page") page : Int,
        @Path("id") actorId : Int,
        @Query("sort") sort : String? = null
    ) : Single<MoviePagedListResponse?>?

    @GET("v1/writers/{id}/movies/{page}")
    fun getWriterMovies(
        @Path("page") page : Int,
        @Path("id") writerId : Int,
        @Query("sort") sort : String? = null
    ) : Single<MoviePagedListResponse?>?

    @GET("v1/movies/category/{id}/{page}")
    fun getCategoryMovies(
        @Path("page") page : Int,
        @Path("id") categoryId : Int,
        @Query("sort") sort : String? = null
    ) : Single<MoviePagedListResponse?>?

    @GET("v1/movies/quality/{id}/{page}")
    fun getQualityMovies(
        @Path("page") page : Int
        ,@Path("id") qualityId : Int
    ) : Single<MoviePagedListResponse?>?

    @GET("v1/movies/language/{id}/{page}")
    fun getLanguageMovies(
        @Path("page") page : Int
        ,@Path("id") languageId : Int
    ) : Single<MoviePagedListResponse?>?

    @GET("v1/movies/genre/{id}/{page}")
    fun getGenreMovies(
        @Path("page") page : Int,
        @Path("id") genreId : Int,
        @Query("sort") sort : String? = null
    ) : Single<MoviePagedListResponse?>?

    @GET("v1/movies/year/{year}/{page}")
    fun getYearMovies(
        @Path("year") year : Int,
        @Path("page") page : Int,
        @Query("sort") sort : String? = null
    ) : Single<MoviePagedListResponse?>?

    @GET("v1/movie/{id}")
    fun getMovie(
        @Path("id") movieId : Int
    ) : Single<MovieResponse?>?

    @GET("v1/season/{id}")
    fun getSeason(
        @Path("id") seasonId : Int
    ) : Single<SeasonResponse?>?

    @GET("v1/episode/{id}")
    fun getEpisode(
        @Path("id") episodeId : Int
    ) : Single<EpisodeResponse?>?

    @GET("v1/comments/{movie_id}/{page}")
    fun getMovieComments(
        @Path("movie_id") movieId : Int,
        @Path("page") page : Int
    ) : Single<CommentListResponse?>?

    @POST("v1/fb-token")
    fun registerFbToken(
        @Body registerFbTokenRequest: RegisterFbTokenRequest
    ) : Single<GeneralResponse?>?

    @GET("v1/categories")
    fun getCategories() : Single<CategoryListResponse?>?

    @GET("v1/countries")
    fun getCountries() : Single<CountryListResponse?>?

    @GET("v1/genres")
    fun getGenres() : Single<GenreListResponse?>?

    @GET("v1/languages")
    fun getLanguages() : Single<LanguageListResponse?>?

    @GET("v1/directors/{page}")
    fun getDirectors(
        @Path("page") page: Int
    ) : Single<DirectorListResponse?>?

    @GET("v1/writers/{page}")
    fun getWriters(
        @Path("page") page: Int
    ) : Single<WriterListResponse?>?

    @GET("v1/actors/{page}")
    fun getActors(
        @Path("page") page: Int
    ) : Single<ActorListResponse?>?

    @GET("v1/search/actor")
    fun searchActor(
        @Query("query") query: String?
    ) : Single<ActorListResponse?>?

    @GET("v1/search/director")
    fun searchDirector(
        @Query("query") query: String?
    ) : Single<DirectorListResponse?>?

    @GET("v1/search/writer")
    fun searchWriter(
        @Query("query") query: String?
    ) : Single<WriterListResponse?>?

    @POST("v1/search/movie")
    fun searchMovie(
        @Body searchMovieRequest: SearchMovieRequest?
    ) : Single<MovieListResponse?>?

    @POST("v1/comment/{movie_id}")
    fun postComment(
        @Body postCommentRequest: PostCommentRequest?,@Path("movie_id") movieId: Int
    ) : Single<GeneralResponse?>?

    @POST("v1/register")
    fun signup(
        @Body signupRequest: SignupRequest?
    ) : Single<GeneralResponse?>?

    @POST("v1/user/update")
    fun updateAccount(
        @Body updateAccountRequest: UpdateAccountRequest?
    ) : Single<GeneralResponse?>?

    @POST("v1/auth/login")
    fun login(
        @Body loginRequest: LoginRequest?
    ) : Single<LoginResponse?>?

    @POST("v1/auth/login/otp")
    fun sendPhoneNumber(
        @Body phoneNumberRequest: PhoneNumberRequest?
    ) : Single<EnterPhoneNumberResponse?>?

    @POST("v2/auth/login/otp")
    fun sendPhoneNumberV2(
        @Body phoneNumberRequest: PhoneNumberRequest?
    ) : Single<EnterPhoneNumberResponse?>?

    @POST("v1/auth/login/otp/verify")
    fun loginWithPhoneNumber(
        @Body loginWithPhoneNumberRequest: LoginWithPhoneNumberRequest?
    ) : Single<LoginResponse?>?

    @POST("v1/otp/verify")
    fun verifyPhoneNumber(
        @Body verifyPhoneNumberRequest: VerifyPhoneNumberRequest?
    ) : Single<UserResponse?>?

    @POST("v1/otp/request")
    fun addPhoneNumber(
        @Body phoneNumberRequest: PhoneNumberRequest?
    ) : Single<EnterPhoneNumberResponse?>?

    @POST()
    fun refreshToken(@Body request: RefreshTokenRequest, @Url url: String = BuildConfig.SERVER_ADDRESS + "oauth/token"): Single<RefreshTokenResponse?>?

    @POST("v1/password/reset")
    fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest) : Single<GeneralResponse?>?

    @PUT("v1/likes/like/{id}")
    fun likeMovie(@Path("id") movieId: Int) : Single<GeneralResponse?>?

    @PUT("v1/likes/dislike/{id}")
    fun dislikeMovie(@Path("id") movieId: Int) : Single<GeneralResponse?>?

    @GET("v1/likes/{page}")
    fun getLikedMovies(
        @Path("page") page : Int,
        @Query("sort") sort : String? = null
    ) : Single<MoviePagedListResponse?>?

    @GET("v1/movie/similar/{id}")
    fun getSimilarMovies(
        @Path("id") movieId : Int
    ) : Single<MovieListResponse?>?

    @GET("v1/actors/{id}")
    fun getActorInfo(
        @Path("id") actorId : Int
    ) : Single<ActorInfoResponse?>?

    @GET("v1/directors/{id}")
    fun getDirectorInfo(
        @Path("id") directorId : Int
    ) : Single<DirectorInfoResponse?>?

    @GET("v1/writers/{id}")
    fun getWriterInfo(
        @Path("id") writerId : Int
    ) : Single<WriterInfoResponse?>?

    @GET("v1/user")
    fun getUserInfo() : Single<UserResponse?>?

    @GET("v1/products")
    fun getProducts() : Single<ProductListResponse?>?

    @POST("v1/payment/pay/{id}")
    fun startPayment(
        @Path("id") productId: Int
    ) : Single<StartPaymentResponse?>?

    @POST("v1/payment/verify")
    fun verifyPayment(
        @Body verifyPaymentRequest: VerifyPaymentRequest
    ) : Single<GeneralResponse?>?

    @POST("v1/movie/request/create")
    fun requestMovie(
        @Body requestMovieRequest: RequestMovieRequest
    ) : Single<GeneralResponse?>?

    @PUT("v1/user/seen/{movie_id}")
    fun updateSeenMovie(
        @Path("movie_id") movieId: Int,
        @Body seenMovieRequest: SeenMovieRequest
    ) : Single<GeneralResponse?>?

    @GET("v1/user/seen/{movie_id}")
    fun getSeenMovie(
        @Path("movie_id") movieId: Int
    ) : Single<SeenMovieResponse?>?

}
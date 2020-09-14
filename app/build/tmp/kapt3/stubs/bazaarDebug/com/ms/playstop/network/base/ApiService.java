package com.ms.playstop.network.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u00c4\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J&\u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0007\u001a\u00020\u0006H\'J\u001c\u0010\b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\t\u0018\u00010\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J\u001c\u0010\n\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u000b\u0018\u00010\u00032\b\b\u0001\u0010\f\u001a\u00020\rH\'J\u0012\u0010\u000e\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u000f\u0018\u00010\u0003H\'J&\u0010\u0010\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0011\u001a\u00020\u0006H\'J\u0012\u0010\u0012\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0013\u0018\u00010\u0003H\'J\u0012\u0010\u0014\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0015\u0018\u00010\u0003H\'J&\u0010\u0016\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u0017\u001a\u00020\u0006H\'J\u001c\u0010\u0018\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0019\u0018\u00010\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J\u001c\u0010\u001a\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u001b\u0018\u00010\u00032\b\b\u0001\u0010\u001c\u001a\u00020\u0006H\'J&\u0010\u001d\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\u001e\u001a\u00020\u0006H\'J\u0012\u0010\u001f\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010 \u0018\u00010\u0003H\'J&\u0010!\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010\"\u001a\u00020\u0006H\'J\u0012\u0010#\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010$\u0018\u00010\u0003H\'J\u001c\u0010%\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J\u001c\u0010&\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\'\u0018\u00010\u00032\b\b\u0001\u0010(\u001a\u00020\u0006H\'J&\u0010)\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010*\u0018\u00010\u00032\b\b\u0001\u0010(\u001a\u00020\u00062\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J&\u0010+\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u0010,\u001a\u00020\u0006H\'J\u001c\u0010-\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010.\u0018\u00010\u00032\b\b\u0001\u0010/\u001a\u00020\u0006H\'J\u001c\u00100\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J&\u00101\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u00102\u001a\u00020\u0006H\'J&\u00103\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u00104\u001a\u00020\u0006H\'J\u001c\u00105\u001a\f\u0012\u0006\u0012\u0004\u0018\u000106\u0018\u00010\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\'J&\u00107\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u00062\b\b\u0001\u00108\u001a\u00020\u0006H\'J\u001e\u00109\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010:\u0018\u00010\u00032\n\b\u0001\u0010;\u001a\u0004\u0018\u00010<H\'J(\u0010=\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010>\u0018\u00010\u00032\n\b\u0001\u0010?\u001a\u0004\u0018\u00010@2\b\b\u0001\u0010(\u001a\u00020\u0006H\'J&\u0010A\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010B\u0018\u00010\u00032\b\b\u0001\u0010C\u001a\u00020D2\b\b\u0003\u0010E\u001a\u00020FH\'J\u001c\u0010G\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010>\u0018\u00010\u00032\b\b\u0001\u0010H\u001a\u00020IH\'J\u001e\u0010J\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\t\u0018\u00010\u00032\n\b\u0001\u0010K\u001a\u0004\u0018\u00010FH\'J\u001e\u0010L\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0019\u0018\u00010\u00032\n\b\u0001\u0010K\u001a\u0004\u0018\u00010FH\'J\u001e\u0010M\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010N\u0018\u00010\u00032\n\b\u0001\u0010K\u001a\u0004\u0018\u00010FH\'J\u001e\u0010O\u001a\f\u0012\u0006\u0012\u0004\u0018\u000106\u0018\u00010\u00032\n\b\u0001\u0010K\u001a\u0004\u0018\u00010FH\'J\u001e\u0010P\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010 \u0018\u00010\u00032\n\b\u0001\u0010Q\u001a\u0004\u0018\u00010RH\'\u00a8\u0006S"}, d2 = {"Lcom/ms/playstop/network/base/ApiService;", "", "getActorMovies", "Lio/reactivex/Single;", "Lcom/ms/playstop/network/model/MoviePagedListResponse;", "page", "", "actorId", "getActors", "Lcom/ms/playstop/network/model/ActorListResponse;", "getAllSuggestionsMovies", "Lcom/ms/playstop/network/model/AllSuggestionsMoviesResponse;", "getAllSuggestionsMoviesRequest", "Lcom/ms/playstop/network/model/GetAllSuggestionsMoviesRequest;", "getCategories", "Lcom/ms/playstop/network/model/CategoryListResponse;", "getCategoryMovies", "categoryId", "getConfig", "Lcom/ms/playstop/network/model/ConfigResponse;", "getCountries", "Lcom/ms/playstop/network/model/CountryListResponse;", "getDirectorMovies", "directorId", "getDirectors", "Lcom/ms/playstop/network/model/DirectorListResponse;", "getEpisode", "Lcom/ms/playstop/network/model/EpisodeResponse;", "episodeId", "getGenreMovies", "genreId", "getGenres", "Lcom/ms/playstop/network/model/GenreListResponse;", "getLanguageMovies", "languageId", "getLanguages", "Lcom/ms/playstop/network/model/LanguageListResponse;", "getLastMovies", "getMovie", "Lcom/ms/playstop/network/model/MovieResponse;", "movieId", "getMovieComments", "Lcom/ms/playstop/network/model/CommentListResponse;", "getQualityMovies", "qualityId", "getSeason", "Lcom/ms/playstop/network/model/SeasonResponse;", "seasonId", "getSpecialMovies", "getSuggestionMovies", "suggestionId", "getWriterMovies", "writerId", "getWriters", "Lcom/ms/playstop/network/model/WriterListResponse;", "getYearMovies", "year", "login", "Lcom/ms/playstop/network/model/LoginResponse;", "loginRequest", "Lcom/ms/playstop/network/model/LoginRequest;", "postComment", "Lcom/ms/playstop/network/model/GeneralResponse;", "postCommentRequest", "Lcom/ms/playstop/network/model/PostCommentRequest;", "refreshToken", "Lcom/ms/playstop/network/model/RefreshTokenResponse;", "request", "Lcom/ms/playstop/network/model/RefreshTokenRequest;", "url", "", "registerFbToken", "registerFbTokenRequest", "Lcom/ms/playstop/network/model/RegisterFbTokenRequest;", "searchActor", "query", "searchDirector", "searchMovie", "Lcom/ms/playstop/network/model/MovieListResponse;", "searchWriter", "signup", "signupRequest", "Lcom/ms/playstop/network/model/SignupRequest;", "app_bazaarDebug"})
public abstract interface ApiService {
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "config")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.ConfigResponse> getConfig();
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST(value = "movies/suggestions")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.AllSuggestionsMoviesResponse> getAllSuggestionsMovies(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Body()
    com.ms.playstop.network.model.GetAllSuggestionsMoviesRequest getAllSuggestionsMoviesRequest);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "movies/{page}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.MoviePagedListResponse> getLastMovies(@retrofit2.http.Path(value = "page")
    int page);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "movies/{page}/{suggestion_id}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.MoviePagedListResponse> getSuggestionMovies(@retrofit2.http.Path(value = "page")
    int page, @retrofit2.http.Path(value = "suggestion_id")
    int suggestionId);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "movies/special/1/{page}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.MoviePagedListResponse> getSpecialMovies(@retrofit2.http.Path(value = "page")
    int page);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "movies/director/{id}/{page}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.MoviePagedListResponse> getDirectorMovies(@retrofit2.http.Path(value = "page")
    int page, @retrofit2.http.Path(value = "id")
    int directorId);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "movies/actor/{id}/{page}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.MoviePagedListResponse> getActorMovies(@retrofit2.http.Path(value = "page")
    int page, @retrofit2.http.Path(value = "id")
    int actorId);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "movies/writer/{id}/{page}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.MoviePagedListResponse> getWriterMovies(@retrofit2.http.Path(value = "page")
    int page, @retrofit2.http.Path(value = "id")
    int writerId);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "movies/category/{id}/{page}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.MoviePagedListResponse> getCategoryMovies(@retrofit2.http.Path(value = "page")
    int page, @retrofit2.http.Path(value = "id")
    int categoryId);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "movies/quality/{id}/{page}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.MoviePagedListResponse> getQualityMovies(@retrofit2.http.Path(value = "page")
    int page, @retrofit2.http.Path(value = "id")
    int qualityId);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "movies/language/{id}/{page}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.MoviePagedListResponse> getLanguageMovies(@retrofit2.http.Path(value = "page")
    int page, @retrofit2.http.Path(value = "id")
    int languageId);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "movies/genre/{id}/{page}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.MoviePagedListResponse> getGenreMovies(@retrofit2.http.Path(value = "page")
    int page, @retrofit2.http.Path(value = "id")
    int genreId);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "movies/year/{year}/{page}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.MoviePagedListResponse> getYearMovies(@retrofit2.http.Path(value = "page")
    int page, @retrofit2.http.Path(value = "year")
    int year);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "movie/{id}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.MovieResponse> getMovie(@retrofit2.http.Path(value = "id")
    int movieId);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "season/{id}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.SeasonResponse> getSeason(@retrofit2.http.Path(value = "id")
    int seasonId);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "episode/{id}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.EpisodeResponse> getEpisode(@retrofit2.http.Path(value = "id")
    int episodeId);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "comments/{movie_id}/{page}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.CommentListResponse> getMovieComments(@retrofit2.http.Path(value = "movie_id")
    int movieId, @retrofit2.http.Path(value = "page")
    int page);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST(value = "fb-token")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.GeneralResponse> registerFbToken(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Body()
    com.ms.playstop.network.model.RegisterFbTokenRequest registerFbTokenRequest);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "categories")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.CategoryListResponse> getCategories();
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "countries")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.CountryListResponse> getCountries();
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "genres")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.GenreListResponse> getGenres();
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "languages")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.LanguageListResponse> getLanguages();
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "directors/{page}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.DirectorListResponse> getDirectors(@retrofit2.http.Path(value = "page")
    int page);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "writers/{page}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.WriterListResponse> getWriters(@retrofit2.http.Path(value = "page")
    int page);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "actors/{page}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.ActorListResponse> getActors(@retrofit2.http.Path(value = "page")
    int page);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "search/actor")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.ActorListResponse> searchActor(@org.jetbrains.annotations.Nullable()
    @retrofit2.http.Query(value = "query")
    java.lang.String query);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "search/director")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.DirectorListResponse> searchDirector(@org.jetbrains.annotations.Nullable()
    @retrofit2.http.Query(value = "query")
    java.lang.String query);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "search/writer")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.WriterListResponse> searchWriter(@org.jetbrains.annotations.Nullable()
    @retrofit2.http.Query(value = "query")
    java.lang.String query);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.GET(value = "search/movie")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.MovieListResponse> searchMovie(@org.jetbrains.annotations.Nullable()
    @retrofit2.http.Query(value = "query")
    java.lang.String query);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST(value = "comment/{movie_id}")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.GeneralResponse> postComment(@org.jetbrains.annotations.Nullable()
    @retrofit2.http.Body()
    com.ms.playstop.network.model.PostCommentRequest postCommentRequest, @retrofit2.http.Path(value = "movie_id")
    int movieId);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST(value = "register")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.GenreListResponse> signup(@org.jetbrains.annotations.Nullable()
    @retrofit2.http.Body()
    com.ms.playstop.network.model.SignupRequest signupRequest);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST(value = "auth/login")
    public abstract io.reactivex.Single<com.ms.playstop.network.model.LoginResponse> login(@org.jetbrains.annotations.Nullable()
    @retrofit2.http.Body()
    com.ms.playstop.network.model.LoginRequest loginRequest);
    
    @org.jetbrains.annotations.Nullable()
    @retrofit2.http.POST()
    public abstract io.reactivex.Single<com.ms.playstop.network.model.RefreshTokenResponse> refreshToken(@org.jetbrains.annotations.NotNull()
    @retrofit2.http.Body()
    com.ms.playstop.network.model.RefreshTokenRequest request, @org.jetbrains.annotations.NotNull()
    @retrofit2.http.Url()
    java.lang.String url);
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 3)
    public final class DefaultImpls {
    }
}
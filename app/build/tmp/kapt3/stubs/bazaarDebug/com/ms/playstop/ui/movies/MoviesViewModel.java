package com.ms.playstop.ui.movies;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0010R&\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR \u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\r\u0010\b\"\u0004\b\u000e\u0010\nR \u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u00100\u0004X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\b\"\u0004\b\u0012\u0010\n\u00a8\u0006\u0018"}, d2 = {"Lcom/ms/playstop/ui/movies/MoviesViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "movies", "Landroidx/lifecycle/LiveData;", "Landroidx/paging/PagedList;", "Lcom/ms/playstop/model/Movie;", "getMovies", "()Landroidx/lifecycle/LiveData;", "setMovies", "(Landroidx/lifecycle/LiveData;)V", "moviesError", "Lcom/ms/playstop/network/model/GeneralResponse;", "getMoviesError", "setMoviesError", "moviesRequestState", "", "getMoviesRequestState", "setMoviesRequestState", "setRequestType", "", "requestType", "Lcom/ms/playstop/ui/movies/adapter/RequestType;", "requestId", "app_bazaarDebug"})
public final class MoviesViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.LiveData<androidx.paging.PagedList<com.ms.playstop.model.Movie>> movies;
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.LiveData<com.ms.playstop.network.model.GeneralResponse> moviesError;
    @org.jetbrains.annotations.NotNull()
    private androidx.lifecycle.LiveData<java.lang.Integer> moviesRequestState;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<androidx.paging.PagedList<com.ms.playstop.model.Movie>> getMovies() {
        return null;
    }
    
    public final void setMovies(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.LiveData<androidx.paging.PagedList<com.ms.playstop.model.Movie>> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<com.ms.playstop.network.model.GeneralResponse> getMoviesError() {
        return null;
    }
    
    public final void setMoviesError(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.LiveData<com.ms.playstop.network.model.GeneralResponse> p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.LiveData<java.lang.Integer> getMoviesRequestState() {
        return null;
    }
    
    public final void setMoviesRequestState(@org.jetbrains.annotations.NotNull()
    androidx.lifecycle.LiveData<java.lang.Integer> p0) {
    }
    
    public final void setRequestType(@org.jetbrains.annotations.NotNull()
    com.ms.playstop.ui.movies.adapter.RequestType requestType, int requestId) {
    }
    
    public MoviesViewModel() {
        super();
    }
}
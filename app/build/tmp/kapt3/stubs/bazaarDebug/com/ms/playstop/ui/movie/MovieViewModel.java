package com.ms.playstop.ui.movie;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u000b\u001a\u00020\fH\u0007J\u0010\u0010\r\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014R\u0019\u0010\u0003\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0017\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0007R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\t0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0007R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\t0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0007\u00a8\u0006\u0015"}, d2 = {"Lcom/ms/playstop/ui/movie/MovieViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "movie", "Landroidx/lifecycle/MutableLiveData;", "Lcom/ms/playstop/model/Movie;", "getMovie", "()Landroidx/lifecycle/MutableLiveData;", "movieError", "Lcom/ms/playstop/network/model/GeneralResponse;", "getMovieError", "movieId", "", "sendComment", "getSendComment", "sendCommentError", "getSendCommentError", "fetchMovie", "", "text", "", "app_bazaarDebug"})
public final class MovieViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.ms.playstop.model.Movie> movie = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.ms.playstop.network.model.GeneralResponse> movieError = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.ms.playstop.network.model.GeneralResponse> sendComment = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.ms.playstop.network.model.GeneralResponse> sendCommentError = null;
    private int movieId;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<com.ms.playstop.model.Movie> getMovie() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<com.ms.playstop.network.model.GeneralResponse> getMovieError() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<com.ms.playstop.network.model.GeneralResponse> getSendComment() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<com.ms.playstop.network.model.GeneralResponse> getSendCommentError() {
        return null;
    }
    
    @android.annotation.SuppressLint(value = {"CheckResult"})
    public final void fetchMovie(int movieId) {
    }
    
    public final void sendComment(@org.jetbrains.annotations.Nullable()
    java.lang.String text) {
    }
    
    public MovieViewModel() {
        super();
    }
}
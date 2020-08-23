package com.ms.playstop.ui.movieLists;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\r\u001a\u00020\u000eJ\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u000bH\u0007R\u001d\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u001d\u0010\t\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\n0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\b\u00a8\u0006\u0010"}, d2 = {"Lcom/ms/playstop/ui/movieLists/MovieListsViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "moviesList", "Landroidx/lifecycle/MutableLiveData;", "", "Lcom/ms/playstop/model/SuggestionMovies;", "getMoviesList", "()Landroidx/lifecycle/MutableLiveData;", "suggestions", "Ljava/util/ArrayList;", "Lcom/ms/playstop/model/Suggestion;", "getSuggestions", "fetchMovies", "", "suggestion", "app_bazaarDebug"})
public final class MovieListsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.ArrayList<com.ms.playstop.model.Suggestion>> suggestions = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.ms.playstop.model.SuggestionMovies>> moviesList = null;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.util.ArrayList<com.ms.playstop.model.Suggestion>> getSuggestions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.ms.playstop.model.SuggestionMovies>> getMoviesList() {
        return null;
    }
    
    public final void fetchMovies() {
    }
    
    @android.annotation.SuppressLint(value = {"CheckResult"})
    public final void fetchMovies(@org.jetbrains.annotations.NotNull()
    com.ms.playstop.model.Suggestion suggestion) {
    }
    
    public MovieListsViewModel() {
        super();
    }
}
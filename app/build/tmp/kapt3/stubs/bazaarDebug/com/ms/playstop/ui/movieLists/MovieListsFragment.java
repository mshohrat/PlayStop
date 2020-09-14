package com.ms.playstop.ui.movieLists;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \"2\u00020\u00012\u00020\u00022\u00020\u0003:\u0001\"B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u000b\u001a\u00020\u0006H\u0016J\b\u0010\f\u001a\u00020\rH\u0016J\b\u0010\u000e\u001a\u00020\u000fH\u0002J\u0012\u0010\u0010\u001a\u00020\u000f2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0016J&\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0015\u001a\u00020\u00162\b\u0010\u0017\u001a\u0004\u0018\u00010\u00182\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012H\u0016J\u0012\u0010\u0019\u001a\u00020\u000f2\b\u0010\u001a\u001a\u0004\u0018\u00010\u001bH\u0016J\u0010\u0010\u001c\u001a\u00020\u000f2\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\b\u0010\u001f\u001a\u00020\u000fH\u0002J\b\u0010 \u001a\u00020\u000fH\u0002J\b\u0010!\u001a\u00020\bH\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/ms/playstop/ui/movieLists/MovieListsFragment;", "Lcom/ms/playstop/base/BaseFragment;", "Lcom/ms/playstop/ui/movieLists/adapter/MovieListAdapter$OnItemClickListener;", "Lcom/ms/playstop/ui/movieLists/adapter/MovieHeaderAdapter$OnItemClickListener;", "()V", "appbarHeight", "", "specialMoviesName", "", "viewModel", "Lcom/ms/playstop/ui/movieLists/MovieListsViewModel;", "containerId", "handleBack", "", "initViews", "", "onActivityCreated", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onMovieClick", "movie", "Lcom/ms/playstop/model/Movie;", "onShowAllClick", "suggestion", "Lcom/ms/playstop/model/Suggestion;", "subscribeToViewEvents", "subscribeToViewModel", "tag", "Companion", "app_bazaarDebug"})
public final class MovieListsFragment extends com.ms.playstop.base.BaseFragment implements com.ms.playstop.ui.movieLists.adapter.MovieListAdapter.OnItemClickListener, com.ms.playstop.ui.movieLists.adapter.MovieHeaderAdapter.OnItemClickListener {
    private com.ms.playstop.ui.movieLists.MovieListsViewModel viewModel;
    private java.lang.String specialMoviesName;
    private int appbarHeight;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TAG = "Movie Lists Fragment";
    public static final com.ms.playstop.ui.movieLists.MovieListsFragment.Companion Companion = null;
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.Nullable()
    @java.lang.Override()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String tag() {
        return null;
    }
    
    @java.lang.Override()
    public void onActivityCreated(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void initViews() {
    }
    
    private final void subscribeToViewModel() {
    }
    
    private final void subscribeToViewEvents() {
    }
    
    @java.lang.Override()
    public void onShowAllClick(@org.jetbrains.annotations.NotNull()
    com.ms.playstop.model.Suggestion suggestion) {
    }
    
    @java.lang.Override()
    public void onMovieClick(@org.jetbrains.annotations.Nullable()
    com.ms.playstop.model.Movie movie) {
    }
    
    @java.lang.Override()
    public int containerId() {
        return 0;
    }
    
    @java.lang.Override()
    public boolean handleBack() {
        return false;
    }
    
    public MovieListsFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/ms/playstop/ui/movieLists/MovieListsFragment$Companion;", "", "()V", "TAG", "", "newInstance", "Lcom/ms/playstop/ui/movieLists/MovieListsFragment;", "app_bazaarDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final com.ms.playstop.ui.movieLists.MovieListsFragment newInstance() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}
package com.ms.playstop.ui.movies;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000 \u00192\u00020\u00012\u00020\u00022\u00020\u0003:\u0001\u0019B\u0005\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0002J\u0012\u0010\t\u001a\u00020\b2\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0016J&\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u00112\b\u0010\n\u001a\u0004\u0018\u00010\u000bH\u0016J\u0012\u0010\u0012\u001a\u00020\b2\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014H\u0016J\b\u0010\u0015\u001a\u00020\bH\u0002J\b\u0010\u0016\u001a\u00020\bH\u0002J\b\u0010\u0017\u001a\u00020\u0018H\u0016R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001a"}, d2 = {"Lcom/ms/playstop/ui/movies/MoviesFragment;", "Lcom/ms/playstop/base/BaseFragment;", "Lcom/ms/playstop/ui/movies/adapter/MoviePagedAdapter$OnItemClickListener;", "Lcom/ms/playstop/ui/movieLists/adapter/MovieAdapter$OnItemClickListener;", "()V", "viewModel", "Lcom/ms/playstop/ui/movies/MoviesViewModel;", "initViews", "", "onActivityCreated", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onItemClick", "movie", "Lcom/ms/playstop/model/Movie;", "subscribeToViewEvents", "subscribeToViewModel", "tag", "", "Companion", "app_bazaarDebug"})
public final class MoviesFragment extends com.ms.playstop.base.BaseFragment implements com.ms.playstop.ui.movies.adapter.MoviePagedAdapter.OnItemClickListener, com.ms.playstop.ui.movieLists.adapter.MovieAdapter.OnItemClickListener {
    private com.ms.playstop.ui.movies.MoviesViewModel viewModel;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MOVIES_REQUEST_TYPE = "MOVIES REQUEST TYPE";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MOVIES_REQUEST_ID = "MOVIES REQUEST ID";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MOVIES_REQUEST_NAME = "MOVIES REQUEST NAME";
    public static final com.ms.playstop.ui.movies.MoviesFragment.Companion Companion = null;
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
    
    private final void subscribeToViewEvents() {
    }
    
    private final void subscribeToViewModel() {
    }
    
    @java.lang.Override()
    public void onItemClick(@org.jetbrains.annotations.Nullable()
    com.ms.playstop.model.Movie movie) {
    }
    
    public MoviesFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0007\u001a\u00020\bR\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/ms/playstop/ui/movies/MoviesFragment$Companion;", "", "()V", "MOVIES_REQUEST_ID", "", "MOVIES_REQUEST_NAME", "MOVIES_REQUEST_TYPE", "newInstance", "Lcom/ms/playstop/ui/movies/MoviesFragment;", "app_bazaarDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final com.ms.playstop.ui.movies.MoviesFragment newInstance() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}
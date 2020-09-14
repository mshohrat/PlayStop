package com.ms.playstop.ui.movie;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000 \"2\u00020\u00012\u00020\u0002:\u0001\"B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\n\u001a\u00020\u000bH\u0002J\b\u0010\f\u001a\u00020\u000bH\u0002J\u0012\u0010\r\u001a\u00020\u000b2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016J&\u0010\u0010\u001a\u0004\u0018\u00010\u00112\u0006\u0010\u0012\u001a\u00020\u00132\b\u0010\u0014\u001a\u0004\u0018\u00010\u00152\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016J\u0010\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u0010\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\u0018H\u0002J\b\u0010\u001a\u001a\u00020\u000bH\u0002J\u0010\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0002J\b\u0010\u001e\u001a\u00020\u000bH\u0002J\b\u0010\u001f\u001a\u00020\u000bH\u0002J\b\u0010 \u001a\u00020!H\u0016R\u0010\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006#"}, d2 = {"Lcom/ms/playstop/ui/movie/MovieFragment;", "Lcom/ms/playstop/base/BaseFragment;", "Lcom/ms/playstop/ui/movie/adapter/EpisodeAdapter$OnItemClickListener;", "()V", "loadingDialog", "Lcom/ms/playstop/utils/LoadingDialog;", "movie", "Lcom/ms/playstop/model/Movie;", "viewModel", "Lcom/ms/playstop/ui/movie/MovieViewModel;", "dismissLoadingDialog", "", "initViews", "onActivityCreated", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onItemClick", "episode", "Lcom/ms/playstop/model/Episode;", "showBottomSheetDialogForUrls", "showLoadingDialog", "showToast", "response", "Lcom/ms/playstop/network/model/GeneralResponse;", "subscribeToViewEvents", "subscribeToViewModel", "tag", "", "Companion", "app_bazaarDebug"})
public final class MovieFragment extends com.ms.playstop.base.BaseFragment implements com.ms.playstop.ui.movie.adapter.EpisodeAdapter.OnItemClickListener {
    private com.ms.playstop.ui.movie.MovieViewModel viewModel;
    private com.ms.playstop.model.Movie movie;
    private com.ms.playstop.utils.LoadingDialog loadingDialog;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MOVIE_ID_KEY = "MOVIE ID KEY";
    public static final com.ms.playstop.ui.movie.MovieFragment.Companion Companion = null;
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
    public void onItemClick(@org.jetbrains.annotations.NotNull()
    com.ms.playstop.model.Episode episode) {
    }
    
    private final void showBottomSheetDialogForUrls(com.ms.playstop.model.Episode episode) {
    }
    
    private final void showToast(com.ms.playstop.network.model.GeneralResponse response) {
    }
    
    private final void showLoadingDialog() {
    }
    
    private final void dismissLoadingDialog() {
    }
    
    public MovieFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0005\u001a\u00020\u0006R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/ms/playstop/ui/movie/MovieFragment$Companion;", "", "()V", "MOVIE_ID_KEY", "", "newInstance", "Lcom/ms/playstop/ui/movie/MovieFragment;", "app_bazaarDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final com.ms.playstop.ui.movie.MovieFragment newInstance() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}
package com.ms.playstop.ui.search;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000 \'2\u00020\u00012\u00020\u0002:\u0001\'B\u0005\u00a2\u0006\u0002\u0010\u0003J\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0002J\b\u0010\u0012\u001a\u00020\u0013H\u0016J\u0012\u0010\u0014\u001a\u00020\u00112\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016J&\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0016J\u0010\u0010\u001d\u001a\u00020\u00112\u0006\u0010\u001e\u001a\u00020\u0013H\u0016J\u0012\u0010\u001f\u001a\u00020\u00112\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\b\u0010\"\u001a\u00020\u0011H\u0002J\b\u0010#\u001a\u00020\u0011H\u0002J\b\u0010$\u001a\u00020\u0011H\u0002J\b\u0010%\u001a\u00020&H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lcom/ms/playstop/ui/search/SearchFragment;", "Lcom/ms/playstop/base/BaseFragment;", "Lcom/ms/playstop/ui/movieLists/adapter/MovieAdapter$OnItemClickListener;", "()V", "DELAY_THRESHOLD", "", "handler", "Landroid/os/Handler;", "loadingDialog", "Lcom/ms/playstop/utils/LoadingDialog;", "searchRunnable", "Ljava/lang/Runnable;", "viewModel", "Lcom/ms/playstop/ui/search/SearchViewModel;", "containerId", "", "dismissLoadingDialog", "", "handleBack", "", "onActivityCreated", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onHiddenChanged", "hidden", "onItemClick", "movie", "Lcom/ms/playstop/model/Movie;", "showLoadingDialog", "subscribeToViewEvents", "subscribeToViewModel", "tag", "", "Companion", "app_bazaarDebug"})
public final class SearchFragment extends com.ms.playstop.base.BaseFragment implements com.ms.playstop.ui.movieLists.adapter.MovieAdapter.OnItemClickListener {
    private com.ms.playstop.ui.search.SearchViewModel viewModel;
    private com.ms.playstop.utils.LoadingDialog loadingDialog;
    private final long DELAY_THRESHOLD = 800L;
    private final android.os.Handler handler = null;
    private final java.lang.Runnable searchRunnable = null;
    public static final com.ms.playstop.ui.search.SearchFragment.Companion Companion = null;
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
    
    private final void subscribeToViewModel() {
    }
    
    private final void subscribeToViewEvents() {
    }
    
    @java.lang.Override()
    public void onItemClick(@org.jetbrains.annotations.Nullable()
    com.ms.playstop.model.Movie movie) {
    }
    
    @java.lang.Override()
    public void onHiddenChanged(boolean hidden) {
    }
    
    @java.lang.Override()
    public boolean handleBack() {
        return false;
    }
    
    @java.lang.Override()
    public int containerId() {
        return 0;
    }
    
    private final void showLoadingDialog() {
    }
    
    private final void dismissLoadingDialog() {
    }
    
    public SearchFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/ms/playstop/ui/search/SearchFragment$Companion;", "", "()V", "newInstance", "Lcom/ms/playstop/ui/search/SearchFragment;", "app_bazaarDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final com.ms.playstop.ui.search.SearchFragment newInstance() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}
package com.ms.playstop.ui.movieLists.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0002\u0015\u0016B\u001b\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\bJ\b\u0010\t\u001a\u00020\nH\u0016J\u001c\u0010\u000b\u001a\u00020\f2\n\u0010\r\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u000e\u001a\u00020\nH\u0016J\u001c\u0010\u000f\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\nH\u0016J\u0014\u0010\u0013\u001a\u00020\f2\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0017"}, d2 = {"Lcom/ms/playstop/ui/movieLists/adapter/MovieListAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/ms/playstop/ui/movieLists/adapter/MovieListAdapter$ViewHolder;", "movies", "", "Lcom/ms/playstop/model/SuggestionMovies;", "onItemClickListener", "Lcom/ms/playstop/ui/movieLists/adapter/MovieListAdapter$OnItemClickListener;", "(Ljava/util/List;Lcom/ms/playstop/ui/movieLists/adapter/MovieListAdapter$OnItemClickListener;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "updateData", "list", "OnItemClickListener", "ViewHolder", "app_bazaarDebug"})
public final class MovieListAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.ms.playstop.ui.movieLists.adapter.MovieListAdapter.ViewHolder> {
    private java.util.List<com.ms.playstop.model.SuggestionMovies> movies;
    private final com.ms.playstop.ui.movieLists.adapter.MovieListAdapter.OnItemClickListener onItemClickListener = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ms.playstop.ui.movieLists.adapter.MovieListAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.ms.playstop.ui.movieLists.adapter.MovieListAdapter.ViewHolder holder, int position) {
    }
    
    public final void updateData(@org.jetbrains.annotations.NotNull()
    java.util.List<com.ms.playstop.model.SuggestionMovies> list) {
    }
    
    public MovieListAdapter(@org.jetbrains.annotations.NotNull()
    java.util.List<com.ms.playstop.model.SuggestionMovies> movies, @org.jetbrains.annotations.NotNull()
    com.ms.playstop.ui.movieLists.adapter.MovieListAdapter.OnItemClickListener onItemClickListener) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018R\u0019\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0019\u0010\r\u001a\n \u0007*\u0004\u0018\u00010\u000e0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0019\u0010\u0011\u001a\n \u0007*\u0004\u0018\u00010\u00120\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006\u0019"}, d2 = {"Lcom/ms/playstop/ui/movieLists/adapter/MovieListAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/ms/playstop/ui/movieLists/adapter/MovieListAdapter;Landroid/view/View;)V", "recycler", "Landroidx/recyclerview/widget/RecyclerView;", "kotlin.jvm.PlatformType", "getRecycler", "()Landroidx/recyclerview/widget/RecyclerView;", "rootView", "getRootView", "()Landroid/view/View;", "showAllBtn", "Lcom/google/android/material/button/MaterialButton;", "getShowAllBtn", "()Lcom/google/android/material/button/MaterialButton;", "titleTv", "Lcom/google/android/material/textview/MaterialTextView;", "getTitleTv", "()Lcom/google/android/material/textview/MaterialTextView;", "bind", "", "item", "Lcom/ms/playstop/model/SuggestionMovies;", "app_bazaarDebug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final android.view.View rootView = null;
        private final com.google.android.material.textview.MaterialTextView titleTv = null;
        private final com.google.android.material.button.MaterialButton showAllBtn = null;
        private final androidx.recyclerview.widget.RecyclerView recycler = null;
        
        @org.jetbrains.annotations.NotNull()
        public final android.view.View getRootView() {
            return null;
        }
        
        public final com.google.android.material.textview.MaterialTextView getTitleTv() {
            return null;
        }
        
        public final com.google.android.material.button.MaterialButton getShowAllBtn() {
            return null;
        }
        
        public final androidx.recyclerview.widget.RecyclerView getRecycler() {
            return null;
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.ms.playstop.model.SuggestionMovies item) {
        }
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH&\u00a8\u0006\t"}, d2 = {"Lcom/ms/playstop/ui/movieLists/adapter/MovieListAdapter$OnItemClickListener;", "", "onMovieClick", "", "movie", "Lcom/ms/playstop/model/Movie;", "onShowAllClick", "suggestion", "Lcom/ms/playstop/model/Suggestion;", "app_bazaarDebug"})
    public static abstract interface OnItemClickListener {
        
        public abstract void onShowAllClick(@org.jetbrains.annotations.NotNull()
        com.ms.playstop.model.Suggestion suggestion);
        
        public abstract void onMovieClick(@org.jetbrains.annotations.Nullable()
        com.ms.playstop.model.Movie movie);
    }
}
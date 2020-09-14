package com.ms.playstop.ui.movieLists.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0003\u0019\u001a\u001bB5\u0012\u000e\u0010\u0003\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0004\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\t\u0012\b\b\u0002\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\b\u0010\u000f\u001a\u00020\tH\u0016J\u0010\u0010\u0010\u001a\u00020\t2\u0006\u0010\u0011\u001a\u00020\tH\u0016J\u001c\u0010\u0012\u001a\u00020\u00132\n\u0010\u0014\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0011\u001a\u00020\tH\u0016J\u001c\u0010\u0015\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0018\u001a\u00020\tH\u0016R\u000e\u0010\r\u001a\u00020\tX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\tX\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0003\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u0004\u0018\u00010\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2 = {"Lcom/ms/playstop/ui/movieLists/adapter/MovieAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/ms/playstop/ui/movieLists/adapter/MovieAdapter$ViewHolder;", "movies", "", "Lcom/ms/playstop/model/Movie;", "onItemClickListener", "Lcom/ms/playstop/ui/movieLists/adapter/MovieAdapter$OnItemClickListener;", "initialCount", "", "handleMargin", "", "(Ljava/util/List;Lcom/ms/playstop/ui/movieLists/adapter/MovieAdapter$OnItemClickListener;IZ)V", "TYPE_NORMAL", "TYPE_PLACE_HOLDER", "getItemCount", "getItemViewType", "position", "onBindViewHolder", "", "holder", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "MovieViewHolder", "OnItemClickListener", "ViewHolder", "app_bazaarDebug"})
public final class MovieAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.ms.playstop.ui.movieLists.adapter.MovieAdapter.ViewHolder> {
    private final int TYPE_PLACE_HOLDER = 1;
    private final int TYPE_NORMAL = 2;
    private final java.util.List<com.ms.playstop.model.Movie> movies = null;
    private final com.ms.playstop.ui.movieLists.adapter.MovieAdapter.OnItemClickListener onItemClickListener = null;
    private final int initialCount = 0;
    private final boolean handleMargin = false;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ms.playstop.ui.movieLists.adapter.MovieAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemViewType(int position) {
        return 0;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.ms.playstop.ui.movieLists.adapter.MovieAdapter.ViewHolder holder, int position) {
    }
    
    public MovieAdapter(@org.jetbrains.annotations.NotNull()
    java.util.List<com.ms.playstop.model.Movie> movies, @org.jetbrains.annotations.Nullable()
    com.ms.playstop.ui.movieLists.adapter.MovieAdapter.OnItemClickListener onItemClickListener, int initialCount, boolean handleMargin) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0096\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001c\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\b\b\u0002\u0010\t\u001a\u00020\nH\u0016\u00a8\u0006\u000b"}, d2 = {"Lcom/ms/playstop/ui/movieLists/adapter/MovieAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/ms/playstop/ui/movieLists/adapter/MovieAdapter;Landroid/view/View;)V", "bind", "", "item", "Lcom/ms/playstop/model/Movie;", "isLastItem", "", "app_bazaarDebug"})
    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        
        public void bind(@org.jetbrains.annotations.Nullable()
        com.ms.playstop.model.Movie item, boolean isLastItem) {
        }
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\b\u0086\u0004\u0018\u00002\u00060\u0001R\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\u0002\u0010\u0005J\u001a\u0010\u0018\u001a\u00020\u00192\b\u0010\u001a\u001a\u0004\u0018\u00010\u001b2\u0006\u0010\u001c\u001a\u00020\u001dH\u0016R\u0019\u0010\u0006\u001a\n \b*\u0004\u0018\u00010\u00070\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0019\u0010\u000b\u001a\n \b*\u0004\u0018\u00010\u00070\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0019\u0010\r\u001a\n \b*\u0004\u0018\u00010\u000e0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0019\u0010\u0011\u001a\n \b*\u0004\u0018\u00010\u00070\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\nR\u0011\u0010\u0013\u001a\u00020\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u0019\u0010\u0016\u001a\n \b*\u0004\u0018\u00010\u00070\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\n\u00a8\u0006\u001e"}, d2 = {"Lcom/ms/playstop/ui/movieLists/adapter/MovieAdapter$MovieViewHolder;", "Lcom/ms/playstop/ui/movieLists/adapter/MovieAdapter$ViewHolder;", "Lcom/ms/playstop/ui/movieLists/adapter/MovieAdapter;", "itemView", "Landroid/view/View;", "(Lcom/ms/playstop/ui/movieLists/adapter/MovieAdapter;Landroid/view/View;)V", "freeTv", "Lcom/google/android/material/textview/MaterialTextView;", "kotlin.jvm.PlatformType", "getFreeTv", "()Lcom/google/android/material/textview/MaterialTextView;", "genreTv", "getGenreTv", "imageIv", "Landroidx/appcompat/widget/AppCompatImageView;", "getImageIv", "()Landroidx/appcompat/widget/AppCompatImageView;", "nameTv", "getNameTv", "rootView", "getRootView", "()Landroid/view/View;", "scoreTv", "getScoreTv", "bind", "", "item", "Lcom/ms/playstop/model/Movie;", "isLastItem", "", "app_bazaarDebug"})
    public final class MovieViewHolder extends com.ms.playstop.ui.movieLists.adapter.MovieAdapter.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final android.view.View rootView = null;
        private final androidx.appcompat.widget.AppCompatImageView imageIv = null;
        private final com.google.android.material.textview.MaterialTextView nameTv = null;
        private final com.google.android.material.textview.MaterialTextView genreTv = null;
        private final com.google.android.material.textview.MaterialTextView scoreTv = null;
        private final com.google.android.material.textview.MaterialTextView freeTv = null;
        
        @org.jetbrains.annotations.NotNull()
        public final android.view.View getRootView() {
            return null;
        }
        
        public final androidx.appcompat.widget.AppCompatImageView getImageIv() {
            return null;
        }
        
        public final com.google.android.material.textview.MaterialTextView getNameTv() {
            return null;
        }
        
        public final com.google.android.material.textview.MaterialTextView getGenreTv() {
            return null;
        }
        
        public final com.google.android.material.textview.MaterialTextView getScoreTv() {
            return null;
        }
        
        public final com.google.android.material.textview.MaterialTextView getFreeTv() {
            return null;
        }
        
        @java.lang.Override()
        public void bind(@org.jetbrains.annotations.Nullable()
        com.ms.playstop.model.Movie item, boolean isLastItem) {
        }
        
        public MovieViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/ms/playstop/ui/movieLists/adapter/MovieAdapter$OnItemClickListener;", "", "onItemClick", "", "movie", "Lcom/ms/playstop/model/Movie;", "app_bazaarDebug"})
    public static abstract interface OnItemClickListener {
        
        public abstract void onItemClick(@org.jetbrains.annotations.Nullable()
        com.ms.playstop.model.Movie movie);
    }
}
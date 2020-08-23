package com.ms.playstop.ui.movie.adapter;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u0011B\u0013\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0007\u001a\u00020\bH\u0016J\u001c\u0010\t\u001a\u00020\n2\n\u0010\u000b\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\f\u001a\u00020\bH\u0016J\u001c\u0010\r\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\bH\u0016R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2 = {"Lcom/ms/playstop/ui/movie/adapter/LinkAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/ms/playstop/ui/movie/adapter/LinkAdapter$ViewHolder;", "urls", "", "Lcom/ms/playstop/model/Url;", "(Ljava/util/List;)V", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "ViewHolder", "app_bazaarDebug"})
public final class LinkAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.ms.playstop.ui.movie.adapter.LinkAdapter.ViewHolder> {
    private final java.util.List<com.ms.playstop.model.Url> urls = null;
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public com.ms.playstop.ui.movie.adapter.LinkAdapter.ViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull()
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override()
    public int getItemCount() {
        return 0;
    }
    
    @java.lang.Override()
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull()
    com.ms.playstop.ui.movie.adapter.LinkAdapter.ViewHolder holder, int position) {
    }
    
    public LinkAdapter(@org.jetbrains.annotations.NotNull()
    java.util.List<com.ms.playstop.model.Url> urls) {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010R\u0019\u0010\u0005\u001a\n \u0007*\u0004\u0018\u00010\u00060\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\f\u00a8\u0006\u0011"}, d2 = {"Lcom/ms/playstop/ui/movie/adapter/LinkAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lcom/ms/playstop/ui/movie/adapter/LinkAdapter;Landroid/view/View;)V", "nameTv", "Lcom/google/android/material/textview/MaterialTextView;", "kotlin.jvm.PlatformType", "getNameTv", "()Lcom/google/android/material/textview/MaterialTextView;", "rootView", "getRootView", "()Landroid/view/View;", "bind", "", "url", "Lcom/ms/playstop/model/Url;", "app_bazaarDebug"})
    public final class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull()
        private final android.view.View rootView = null;
        private final com.google.android.material.textview.MaterialTextView nameTv = null;
        
        @org.jetbrains.annotations.NotNull()
        public final android.view.View getRootView() {
            return null;
        }
        
        public final com.google.android.material.textview.MaterialTextView getNameTv() {
            return null;
        }
        
        public final void bind(@org.jetbrains.annotations.NotNull()
        com.ms.playstop.model.Url url) {
        }
        
        public ViewHolder(@org.jetbrains.annotations.NotNull()
        android.view.View itemView) {
            super(null);
        }
    }
}
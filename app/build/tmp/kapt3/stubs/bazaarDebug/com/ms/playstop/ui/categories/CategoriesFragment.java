package com.ms.playstop.ui.categories;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0002\u0018\u0000  2\u00020\u0001:\u0001 B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\n\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0002J\b\u0010\t\u001a\u00020\nH\u0016J\u0012\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016J&\u0010\u000f\u001a\u0004\u0018\u00010\u00102\u0006\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u00142\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0016J\u0010\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0016\u001a\u00020\u0017H\u0002J\u0010\u0010\u0015\u001a\u00020\f2\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u0010\u0010\u0015\u001a\u00020\f2\u0006\u0010\u001a\u001a\u00020\u001bH\u0002J\b\u0010\u001c\u001a\u00020\fH\u0002J\b\u0010\u001d\u001a\u00020\fH\u0002J\b\u0010\u001e\u001a\u00020\u001fH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006!"}, d2 = {"Lcom/ms/playstop/ui/categories/CategoriesFragment;", "Lcom/ms/playstop/base/BaseFragment;", "()V", "viewModel", "Lcom/ms/playstop/ui/categories/CategoriesViewModel;", "containerId", "", "createButton", "Lcom/google/android/material/button/MaterialButton;", "handleBack", "", "onActivityCreated", "", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onItemClick", "category", "Lcom/ms/playstop/model/Category;", "genre", "Lcom/ms/playstop/model/Genre;", "suggestion", "Lcom/ms/playstop/model/Suggestion;", "subscribeToViewEvents", "subscribeToViewModel", "tag", "", "Companion", "app_bazaarDebug"})
public final class CategoriesFragment extends com.ms.playstop.base.BaseFragment {
    private com.ms.playstop.ui.categories.CategoriesViewModel viewModel;
    public static final com.ms.playstop.ui.categories.CategoriesFragment.Companion Companion = null;
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
    
    private final com.google.android.material.button.MaterialButton createButton() {
        return null;
    }
    
    private final void onItemClick(com.ms.playstop.model.Category category) {
    }
    
    private final void onItemClick(com.ms.playstop.model.Genre genre) {
    }
    
    private final void onItemClick(com.ms.playstop.model.Suggestion suggestion) {
    }
    
    @java.lang.Override()
    public int containerId() {
        return 0;
    }
    
    @java.lang.Override()
    public boolean handleBack() {
        return false;
    }
    
    public CategoriesFragment() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/ms/playstop/ui/categories/CategoriesFragment$Companion;", "", "()V", "newInstance", "Lcom/ms/playstop/ui/categories/CategoriesFragment;", "app_bazaarDebug"})
    public static final class Companion {
        
        @org.jetbrains.annotations.NotNull()
        public final com.ms.playstop.ui.categories.CategoriesFragment newInstance() {
            return null;
        }
        
        private Companion() {
            super();
        }
    }
}
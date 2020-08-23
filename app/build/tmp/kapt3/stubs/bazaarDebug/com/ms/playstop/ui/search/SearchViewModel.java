package com.ms.playstop.ui.search;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0007R\u0017\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u001d\u0010\b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u0007\u00a8\u0006\u0010"}, d2 = {"Lcom/ms/playstop/ui/search/SearchViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "searchError", "Landroidx/lifecycle/MutableLiveData;", "Lcom/ms/playstop/network/model/GeneralResponse;", "getSearchError", "()Landroidx/lifecycle/MutableLiveData;", "searchResult", "", "Lcom/ms/playstop/model/Movie;", "getSearchResult", "searchMovie", "", "query", "", "app_bazaarDebug"})
public final class SearchViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<java.util.List<com.ms.playstop.model.Movie>> searchResult = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.ms.playstop.network.model.GeneralResponse> searchError = null;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<java.util.List<com.ms.playstop.model.Movie>> getSearchResult() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<com.ms.playstop.network.model.GeneralResponse> getSearchError() {
        return null;
    }
    
    @android.annotation.SuppressLint(value = {"CheckResult"})
    public final void searchMovie(@org.jetbrains.annotations.NotNull()
    java.lang.String query) {
    }
    
    public SearchViewModel() {
        super();
    }
}
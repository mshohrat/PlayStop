package com.ms.playstop.ui.splash;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u000b\u001a\u00020\fH\u0007R\u0019\u0010\u0003\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00050\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0019\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0007\u00a8\u0006\r"}, d2 = {"Lcom/ms/playstop/ui/splash/SplashViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "config", "Landroidx/lifecycle/MutableLiveData;", "Lcom/ms/playstop/network/model/ConfigResponse;", "getConfig", "()Landroidx/lifecycle/MutableLiveData;", "configError", "Lcom/ms/playstop/network/model/GeneralResponse;", "getConfigError", "fetchConfig", "", "app_bazaarDebug"})
public final class SplashViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.ms.playstop.network.model.ConfigResponse> config = null;
    @org.jetbrains.annotations.NotNull()
    private final androidx.lifecycle.MutableLiveData<com.ms.playstop.network.model.GeneralResponse> configError = null;
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<com.ms.playstop.network.model.ConfigResponse> getConfig() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final androidx.lifecycle.MutableLiveData<com.ms.playstop.network.model.GeneralResponse> getConfigError() {
        return null;
    }
    
    @android.annotation.SuppressLint(value = {"CheckResult"})
    public final void fetchConfig() {
    }
    
    public SplashViewModel() {
        super();
    }
}
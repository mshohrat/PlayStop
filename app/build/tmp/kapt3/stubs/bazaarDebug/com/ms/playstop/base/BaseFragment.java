package com.ms.playstop.base;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0000\b&\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0003\u001a\u00020\u0004H\u0017J\b\u0010\u0005\u001a\u00020\u0006H\u0016J\b\u0010\u0007\u001a\u00020\bH&\u00a8\u0006\t"}, d2 = {"Lcom/ms/playstop/base/BaseFragment;", "Landroidx/fragment/app/Fragment;", "()V", "containerId", "", "handleBack", "", "tag", "", "app_bazaarDebug"})
public abstract class BaseFragment extends androidx.fragment.app.Fragment {
    private java.util.HashMap _$_findViewCache;
    
    @org.jetbrains.annotations.NotNull()
    public abstract java.lang.String tag();
    
    public boolean handleBack() {
        return false;
    }
    
    @androidx.annotation.IdRes()
    public int containerId() {
        return 0;
    }
    
    public BaseFragment() {
        super();
    }
}
package com.ms.playstop.network.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\b\u0018\u0000 \u00112\u00020\u0001:\u0001\u0011B\u0017\u0012\u0010\u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003\u00a2\u0006\u0002\u0010\u0005J\u0013\u0010\b\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003H\u00c6\u0003J\u001d\u0010\t\u001a\u00020\u00002\u0012\b\u0002\u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u0003H\u00c6\u0001J\u0013\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\r\u001a\u00020\u000eH\u00d6\u0001J\t\u0010\u000f\u001a\u00020\u0010H\u00d6\u0001R \u0010\u0002\u001a\f\u0012\u0006\u0012\u0004\u0018\u00010\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007\u00a8\u0006\u0012"}, d2 = {"Lcom/ms/playstop/network/model/ConfigResponse;", "", "suggestions", "", "Lcom/ms/playstop/model/Suggestion;", "(Ljava/util/List;)V", "getSuggestions", "()Ljava/util/List;", "component1", "copy", "equals", "", "other", "hashCode", "", "toString", "", "Companion", "app_bazaarDebug"})
public final class ConfigResponse {
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "suggestions")
    private final java.util.List<com.ms.playstop.model.Suggestion> suggestions = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SAVE_KEY = "Profile Response Save Key";
    public static final com.ms.playstop.network.model.ConfigResponse.Companion Companion = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Suggestion> getSuggestions() {
        return null;
    }
    
    public ConfigResponse(@org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Suggestion> suggestions) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Suggestion> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ms.playstop.network.model.ConfigResponse copy(@org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Suggestion> suggestions) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    @java.lang.Override()
    public java.lang.String toString() {
        return null;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object p0) {
        return false;
    }
    
    @kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/ms/playstop/network/model/ConfigResponse$Companion;", "", "()V", "SAVE_KEY", "", "app_bazaarDebug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}
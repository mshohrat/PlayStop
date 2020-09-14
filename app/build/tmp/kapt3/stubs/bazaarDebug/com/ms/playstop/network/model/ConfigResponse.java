package com.ms.playstop.network.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0018\n\u0002\u0010\b\n\u0002\b\u0003\b\u0086\b\u0018\u0000 *2\u00020\u0001:\u0001*B]\u0012\u000e\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003\u0012\u000e\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0003\u0012\u000e\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0003\u0012\b\u0010\t\u001a\u0004\u0018\u00010\n\u0012\b\u0010\u000b\u001a\u0004\u0018\u00010\f\u0012\b\u0010\r\u001a\u0004\u0018\u00010\f\u0012\b\u0010\u000e\u001a\u0004\u0018\u00010\u000f\u00a2\u0006\u0002\u0010\u0010J\u0011\u0010\u001c\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0003H\u00c6\u0003J\u0011\u0010\u001d\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u0003H\u00c6\u0003J\u0011\u0010\u001e\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u0003H\u00c6\u0003J\u000b\u0010\u001f\u001a\u0004\u0018\u00010\nH\u00c6\u0003J\u0010\u0010 \u001a\u0004\u0018\u00010\fH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0014J\u0010\u0010!\u001a\u0004\u0018\u00010\fH\u00c6\u0003\u00a2\u0006\u0002\u0010\u0014J\u000b\u0010\"\u001a\u0004\u0018\u00010\u000fH\u00c6\u0003Jt\u0010#\u001a\u00020\u00002\u0010\b\u0002\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00032\u0010\b\u0002\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u00032\u0010\b\u0002\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00032\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\n2\n\b\u0002\u0010\u000b\u001a\u0004\u0018\u00010\f2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\f2\n\b\u0002\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u00c6\u0001\u00a2\u0006\u0002\u0010$J\u0013\u0010%\u001a\u00020\f2\b\u0010&\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\'\u001a\u00020(H\u00d6\u0001J\t\u0010)\u001a\u00020\nH\u00d6\u0001R\u001e\u0010\u0005\u001a\n\u0012\u0004\u0012\u00020\u0006\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u001e\u0010\u0007\u001a\n\u0012\u0004\u0012\u00020\b\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0012R\u001a\u0010\u000b\u001a\u0004\u0018\u00010\f8\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0015\u001a\u0004\b\u000b\u0010\u0014R\u001a\u0010\r\u001a\u0004\u0018\u00010\f8\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010\u0015\u001a\u0004\b\u0016\u0010\u0014R\u0018\u0010\t\u001a\u0004\u0018\u00010\n8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0018R\u001e\u0010\u0002\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u0012R\u0018\u0010\u000e\u001a\u0004\u0018\u00010\u000f8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001b\u00a8\u0006+"}, d2 = {"Lcom/ms/playstop/network/model/ConfigResponse;", "", "suggestions", "", "Lcom/ms/playstop/model/Suggestion;", "categories", "Lcom/ms/playstop/model/Category;", "genres", "Lcom/ms/playstop/model/Genre;", "specialsName", "", "isUserActive", "", "requiresToken", "user", "Lcom/ms/playstop/network/model/UserResponse;", "(Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Lcom/ms/playstop/network/model/UserResponse;)V", "getCategories", "()Ljava/util/List;", "getGenres", "()Ljava/lang/Boolean;", "Ljava/lang/Boolean;", "getRequiresToken", "getSpecialsName", "()Ljava/lang/String;", "getSuggestions", "getUser", "()Lcom/ms/playstop/network/model/UserResponse;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "copy", "(Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/lang/String;Ljava/lang/Boolean;Ljava/lang/Boolean;Lcom/ms/playstop/network/model/UserResponse;)Lcom/ms/playstop/network/model/ConfigResponse;", "equals", "other", "hashCode", "", "toString", "Companion", "app_bazaarDebug"})
public final class ConfigResponse {
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "suggestions")
    private final java.util.List<com.ms.playstop.model.Suggestion> suggestions = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "categories")
    private final java.util.List<com.ms.playstop.model.Category> categories = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "genres")
    private final java.util.List<com.ms.playstop.model.Genre> genres = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "specials")
    private final java.lang.String specialsName = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "is_user_active")
    private final java.lang.Boolean isUserActive = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "requires_token")
    private final java.lang.Boolean requiresToken = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "user")
    private final com.ms.playstop.network.model.UserResponse user = null;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SAVE_KEY = "Config Response Save Key";
    public static final com.ms.playstop.network.model.ConfigResponse.Companion Companion = null;
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Suggestion> getSuggestions() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Category> getCategories() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Genre> getGenres() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getSpecialsName() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean isUserActive() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean getRequiresToken() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ms.playstop.network.model.UserResponse getUser() {
        return null;
    }
    
    public ConfigResponse(@org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Suggestion> suggestions, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Category> categories, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Genre> genres, @org.jetbrains.annotations.Nullable()
    java.lang.String specialsName, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean isUserActive, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean requiresToken, @org.jetbrains.annotations.Nullable()
    com.ms.playstop.network.model.UserResponse user) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Suggestion> component1() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Category> component2() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Genre> component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean component5() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Boolean component6() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ms.playstop.network.model.UserResponse component7() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ms.playstop.network.model.ConfigResponse copy(@org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Suggestion> suggestions, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Category> categories, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Genre> genres, @org.jetbrains.annotations.Nullable()
    java.lang.String specialsName, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean isUserActive, @org.jetbrains.annotations.Nullable()
    java.lang.Boolean requiresToken, @org.jetbrains.annotations.Nullable()
    com.ms.playstop.network.model.UserResponse user) {
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
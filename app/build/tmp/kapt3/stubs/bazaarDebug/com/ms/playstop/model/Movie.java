package com.ms.playstop.model;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 16}, bv = {1, 0, 3}, k = 1, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b=\b\u0086\b\u0018\u00002\u00020\u0001B\u00f1\u0001\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\b\u0010\u0007\u001a\u0004\u0018\u00010\b\u0012\b\u0010\t\u001a\u0004\u0018\u00010\u0003\u0012\u0006\u0010\n\u001a\u00020\u0003\u0012\u0006\u0010\u000b\u001a\u00020\u0003\u0012\u0006\u0010\f\u001a\u00020\b\u0012\b\u0010\r\u001a\u0004\u0018\u00010\u0005\u0012\u0006\u0010\u000e\u001a\u00020\u0005\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\b\u0010\u0011\u001a\u0004\u0018\u00010\u0012\u0012\b\u0010\u0013\u001a\u0004\u0018\u00010\u0014\u0012\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016\u0012\u000e\u0010\u0017\u001a\n\u0012\u0004\u0012\u00020\u0019\u0018\u00010\u0018\u0012\u000e\u0010\u001a\u001a\n\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u0018\u0012\u000e\u0010\u001c\u001a\n\u0012\u0004\u0012\u00020\u001d\u0018\u00010\u0018\u0012\u000e\u0010\u001e\u001a\n\u0012\u0004\u0012\u00020\u001f\u0018\u00010\u0018\u0012\u000e\u0010 \u001a\n\u0012\u0004\u0012\u00020!\u0018\u00010\u0018\u0012\u000e\u0010\"\u001a\n\u0012\u0004\u0012\u00020#\u0018\u00010\u0018\u0012\u000e\u0010$\u001a\n\u0012\u0004\u0012\u00020%\u0018\u00010\u0018\u00a2\u0006\u0002\u0010&J\t\u0010G\u001a\u00020\u0003H\u00c6\u0003J\t\u0010H\u001a\u00020\u0005H\u00c6\u0003J\t\u0010I\u001a\u00020\u0010H\u00c6\u0003J\u000b\u0010J\u001a\u0004\u0018\u00010\u0012H\u00c6\u0003J\u000b\u0010K\u001a\u0004\u0018\u00010\u0014H\u00c6\u0003J\u000b\u0010L\u001a\u0004\u0018\u00010\u0016H\u00c6\u0003J\u0011\u0010M\u001a\n\u0012\u0004\u0012\u00020\u0019\u0018\u00010\u0018H\u00c6\u0003J\u0011\u0010N\u001a\n\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u0018H\u00c6\u0003J\u0011\u0010O\u001a\n\u0012\u0004\u0012\u00020\u001d\u0018\u00010\u0018H\u00c6\u0003J\u0011\u0010P\u001a\n\u0012\u0004\u0012\u00020\u001f\u0018\u00010\u0018H\u00c6\u0003J\u0011\u0010Q\u001a\n\u0012\u0004\u0012\u00020!\u0018\u00010\u0018H\u00c6\u0003J\t\u0010R\u001a\u00020\u0005H\u00c6\u0003J\u0011\u0010S\u001a\n\u0012\u0004\u0012\u00020#\u0018\u00010\u0018H\u00c6\u0003J\u0011\u0010T\u001a\n\u0012\u0004\u0012\u00020%\u0018\u00010\u0018H\u00c6\u0003J\t\u0010U\u001a\u00020\u0005H\u00c6\u0003J\u0010\u0010V\u001a\u0004\u0018\u00010\bH\u00c6\u0003\u00a2\u0006\u0002\u0010=J\u0010\u0010W\u001a\u0004\u0018\u00010\u0003H\u00c6\u0003\u00a2\u0006\u0002\u0010@J\t\u0010X\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Y\u001a\u00020\u0003H\u00c6\u0003J\t\u0010Z\u001a\u00020\bH\u00c6\u0003J\u000b\u0010[\u001a\u0004\u0018\u00010\u0005H\u00c6\u0003J\u00a4\u0002\u0010\\\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00052\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b2\n\b\u0002\u0010\t\u001a\u0004\u0018\u00010\u00032\b\b\u0002\u0010\n\u001a\u00020\u00032\b\b\u0002\u0010\u000b\u001a\u00020\u00032\b\b\u0002\u0010\f\u001a\u00020\b2\n\b\u0002\u0010\r\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u000e\u001a\u00020\u00052\b\b\u0002\u0010\u000f\u001a\u00020\u00102\n\b\u0002\u0010\u0011\u001a\u0004\u0018\u00010\u00122\n\b\u0002\u0010\u0013\u001a\u0004\u0018\u00010\u00142\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00162\u0010\b\u0002\u0010\u0017\u001a\n\u0012\u0004\u0012\u00020\u0019\u0018\u00010\u00182\u0010\b\u0002\u0010\u001a\u001a\n\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u00182\u0010\b\u0002\u0010\u001c\u001a\n\u0012\u0004\u0012\u00020\u001d\u0018\u00010\u00182\u0010\b\u0002\u0010\u001e\u001a\n\u0012\u0004\u0012\u00020\u001f\u0018\u00010\u00182\u0010\b\u0002\u0010 \u001a\n\u0012\u0004\u0012\u00020!\u0018\u00010\u00182\u0010\b\u0002\u0010\"\u001a\n\u0012\u0004\u0012\u00020#\u0018\u00010\u00182\u0010\b\u0002\u0010$\u001a\n\u0012\u0004\u0012\u00020%\u0018\u00010\u0018H\u00c6\u0001\u00a2\u0006\u0002\u0010]J\u0013\u0010^\u001a\u00020\u00102\b\u0010_\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010`\u001a\u00020\u0003H\u00d6\u0001J\t\u0010a\u001a\u00020\u0005H\u00d6\u0001R\u001e\u0010\u001e\u001a\n\u0012\u0004\u0012\u00020\u001f\u0018\u00010\u00188\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\'\u0010(R\u0018\u0010\u0011\u001a\u0004\u0018\u00010\u00128\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b)\u0010*R\u001e\u0010$\u001a\n\u0012\u0004\u0012\u00020%\u0018\u00010\u00188\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b+\u0010(R\u001e\u0010\u001c\u001a\n\u0012\u0004\u0012\u00020\u001d\u0018\u00010\u00188\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b,\u0010(R\u0016\u0010\u0006\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b-\u0010.R\u0018\u0010\u0013\u001a\u0004\u0018\u00010\u00148\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b/\u00100R\u001e\u0010\u0017\u001a\n\u0012\u0004\u0012\u00020\u0019\u0018\u00010\u00188\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b1\u0010(R\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b2\u00103R\u0016\u0010\u000e\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b4\u0010.R\u0016\u0010\u000f\u001a\u00020\u00108\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u00105R\u001e\u0010\u001a\u001a\n\u0012\u0004\u0012\u00020\u001b\u0018\u00010\u00188\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b6\u0010(R\u0016\u0010\u000b\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b7\u00103R\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b8\u0010.R\u0016\u0010\f\u001a\u00020\b8\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b9\u0010:R\u0016\u0010\n\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b;\u00103R\u001a\u0010\u0007\u001a\u0004\u0018\u00010\b8\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010>\u001a\u0004\b<\u0010=R\u001a\u0010\t\u001a\u0004\u0018\u00010\u00038\u0006X\u0087\u0004\u00a2\u0006\n\n\u0002\u0010A\u001a\u0004\b?\u0010@R\u001e\u0010 \u001a\n\u0012\u0004\u0012\u00020!\u0018\u00010\u00188\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bB\u0010(R\u0018\u0010\r\u001a\u0004\u0018\u00010\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bC\u0010.R\u001e\u0010\"\u001a\n\u0012\u0004\u0012\u00020#\u0018\u00010\u00188\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bD\u0010(R\u0018\u0010\u0015\u001a\u0004\u0018\u00010\u00168\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\bE\u0010F\u00a8\u0006b"}, d2 = {"Lcom/ms/playstop/model/Movie;", "", "id", "", "name", "", "description", "score", "", "scoreVotes", "productionYear", "length", "price", "trailer", "image", "isSeries", "", "category", "Lcom/ms/playstop/model/Category;", "director", "Lcom/ms/playstop/model/Director;", "writer", "Lcom/ms/playstop/model/Writer;", "genres", "", "Lcom/ms/playstop/model/Genre;", "languages", "Lcom/ms/playstop/model/Language;", "countries", "Lcom/ms/playstop/model/Country;", "actors", "Lcom/ms/playstop/model/Actor;", "seasons", "Lcom/ms/playstop/model/Season;", "urls", "Lcom/ms/playstop/model/Url;", "comments", "Lcom/ms/playstop/model/Comment;", "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/Float;Ljava/lang/Integer;IIFLjava/lang/String;Ljava/lang/String;ZLcom/ms/playstop/model/Category;Lcom/ms/playstop/model/Director;Lcom/ms/playstop/model/Writer;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;)V", "getActors", "()Ljava/util/List;", "getCategory", "()Lcom/ms/playstop/model/Category;", "getComments", "getCountries", "getDescription", "()Ljava/lang/String;", "getDirector", "()Lcom/ms/playstop/model/Director;", "getGenres", "getId", "()I", "getImage", "()Z", "getLanguages", "getLength", "getName", "getPrice", "()F", "getProductionYear", "getScore", "()Ljava/lang/Float;", "Ljava/lang/Float;", "getScoreVotes", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getSeasons", "getTrailer", "getUrls", "getWriter", "()Lcom/ms/playstop/model/Writer;", "component1", "component10", "component11", "component12", "component13", "component14", "component15", "component16", "component17", "component18", "component19", "component2", "component20", "component21", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "copy", "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/Float;Ljava/lang/Integer;IIFLjava/lang/String;Ljava/lang/String;ZLcom/ms/playstop/model/Category;Lcom/ms/playstop/model/Director;Lcom/ms/playstop/model/Writer;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;Ljava/util/List;)Lcom/ms/playstop/model/Movie;", "equals", "other", "hashCode", "toString", "app_bazaarDebug"})
public final class Movie {
    @com.google.gson.annotations.SerializedName(value = "id")
    private final int id = 0;
    @org.jetbrains.annotations.NotNull()
    @com.google.gson.annotations.SerializedName(value = "name")
    private final java.lang.String name = null;
    @org.jetbrains.annotations.NotNull()
    @com.google.gson.annotations.SerializedName(value = "description")
    private final java.lang.String description = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "score")
    private final java.lang.Float score = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "score_votes")
    private final java.lang.Integer scoreVotes = null;
    @com.google.gson.annotations.SerializedName(value = "production_year")
    private final int productionYear = 0;
    @com.google.gson.annotations.SerializedName(value = "length")
    private final int length = 0;
    @com.google.gson.annotations.SerializedName(value = "price")
    private final float price = 0.0F;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "trailer")
    private final java.lang.String trailer = null;
    @org.jetbrains.annotations.NotNull()
    @com.google.gson.annotations.SerializedName(value = "image")
    private final java.lang.String image = null;
    @com.google.gson.annotations.SerializedName(value = "is_series")
    private final boolean isSeries = false;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "category")
    private final com.ms.playstop.model.Category category = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "director")
    private final com.ms.playstop.model.Director director = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "writer")
    private final com.ms.playstop.model.Writer writer = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "genres")
    private final java.util.List<com.ms.playstop.model.Genre> genres = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "languages")
    private final java.util.List<com.ms.playstop.model.Language> languages = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "countries")
    private final java.util.List<com.ms.playstop.model.Country> countries = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "actors")
    private final java.util.List<com.ms.playstop.model.Actor> actors = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "seasons")
    private final java.util.List<com.ms.playstop.model.Season> seasons = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "urls")
    private final java.util.List<com.ms.playstop.model.Url> urls = null;
    @org.jetbrains.annotations.Nullable()
    @com.google.gson.annotations.SerializedName(value = "comments")
    private final java.util.List<com.ms.playstop.model.Comment> comments = null;
    
    public final int getId() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getName() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getDescription() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Float getScore() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer getScoreVotes() {
        return null;
    }
    
    public final int getProductionYear() {
        return 0;
    }
    
    public final int getLength() {
        return 0;
    }
    
    public final float getPrice() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String getTrailer() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getImage() {
        return null;
    }
    
    public final boolean isSeries() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ms.playstop.model.Category getCategory() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ms.playstop.model.Director getDirector() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ms.playstop.model.Writer getWriter() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Genre> getGenres() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Language> getLanguages() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Country> getCountries() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Actor> getActors() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Season> getSeasons() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Url> getUrls() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Comment> getComments() {
        return null;
    }
    
    public Movie(int id, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.Nullable()
    java.lang.Float score, @org.jetbrains.annotations.Nullable()
    java.lang.Integer scoreVotes, int productionYear, int length, float price, @org.jetbrains.annotations.Nullable()
    java.lang.String trailer, @org.jetbrains.annotations.NotNull()
    java.lang.String image, boolean isSeries, @org.jetbrains.annotations.Nullable()
    com.ms.playstop.model.Category category, @org.jetbrains.annotations.Nullable()
    com.ms.playstop.model.Director director, @org.jetbrains.annotations.Nullable()
    com.ms.playstop.model.Writer writer, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Genre> genres, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Language> languages, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Country> countries, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Actor> actors, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Season> seasons, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Url> urls, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Comment> comments) {
        super();
    }
    
    public final int component1() {
        return 0;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component3() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Float component4() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Integer component5() {
        return null;
    }
    
    public final int component6() {
        return 0;
    }
    
    public final int component7() {
        return 0;
    }
    
    public final float component8() {
        return 0.0F;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.String component9() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component10() {
        return null;
    }
    
    public final boolean component11() {
        return false;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ms.playstop.model.Category component12() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ms.playstop.model.Director component13() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.ms.playstop.model.Writer component14() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Genre> component15() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Language> component16() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Country> component17() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Actor> component18() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Season> component19() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Url> component20() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.util.List<com.ms.playstop.model.Comment> component21() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.ms.playstop.model.Movie copy(int id, @org.jetbrains.annotations.NotNull()
    java.lang.String name, @org.jetbrains.annotations.NotNull()
    java.lang.String description, @org.jetbrains.annotations.Nullable()
    java.lang.Float score, @org.jetbrains.annotations.Nullable()
    java.lang.Integer scoreVotes, int productionYear, int length, float price, @org.jetbrains.annotations.Nullable()
    java.lang.String trailer, @org.jetbrains.annotations.NotNull()
    java.lang.String image, boolean isSeries, @org.jetbrains.annotations.Nullable()
    com.ms.playstop.model.Category category, @org.jetbrains.annotations.Nullable()
    com.ms.playstop.model.Director director, @org.jetbrains.annotations.Nullable()
    com.ms.playstop.model.Writer writer, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Genre> genres, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Language> languages, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Country> countries, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Actor> actors, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Season> seasons, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Url> urls, @org.jetbrains.annotations.Nullable()
    java.util.List<com.ms.playstop.model.Comment> comments) {
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
}
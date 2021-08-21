package com.ms.playstop.ui.movies.adapter

enum class RequestType(val type: Int) {
    SUGGESTION(1),
    CATEGORY(2),
    GENRE(3),
    SPECIAL(4),
    YEAR(5),
    LIKES(6),
    ACTOR(7),
    DIRECTOR(8),
    WRITER(9),
    GENRES_SUGGESTION(10),
}
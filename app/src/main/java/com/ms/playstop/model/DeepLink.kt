package com.ms.playstop.model

data class DeepLink(
    var scheme: Scheme,
    val host: Host,
    val path1: Path? = null,
    val path2: Path? = null,
    val path3: Path? = null
)
package com.example.pintube.data.remote.dto

data class ThumbnailsResponse(
    val default: Default?,
    val high: High?,
    val maxres: Maxres?,
    val medium: Medium?,
    val standard: Standard?
)

data class Default(
    val height: Int?,
    val url: String?,
    val width: Int?
)
data class High(
    val height: Int?,
    val url: String?,
    val width: Int?
)
data class Maxres(
    val height: Int?,
    val url: String?,
    val width: Int?
)
data class Medium(
    val height: Int?,
    val url: String?,
    val width: Int?
)

data class Standard(
    val height: Int?,
    val url: String?,
    val width: Int?
)

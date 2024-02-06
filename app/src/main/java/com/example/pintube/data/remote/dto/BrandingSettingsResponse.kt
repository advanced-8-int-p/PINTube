package com.example.pintube.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BrandingSettingsResponse (
    val channel: Channel?,
    val image: Image?,
    val localizations: Localizations?,
)

data class Localizations (
    @SerializedName("ko_KR")
    val koKR: String?,
    @SerializedName("en_US")
    val enUS: String?
)

data class Image (
    val bannerExternalUrl: String?,
)

data class Channel (
    val title: String?,
    val description: String?,
    val keywords: String?,
    val unsubscribedTrailer: String?,
    val defaultLanguage: String?,
    val country: String?,
)

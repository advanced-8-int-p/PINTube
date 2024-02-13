package com.example.pintube.domain.repository

interface PageTokenPrefRepository {
    fun saveNextPageToken(query: String, page: String, token: String?)

    fun getPageToken(query: String,page: String): String?
}

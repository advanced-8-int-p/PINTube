package com.example.pintube.domain.repository

interface PageTokenPrefRepository {
    fun saveNextPageToken(query: String, page: String, token: String?)
    fun getPageToken(query: String, page: String): String?

    fun saveCategory(key: String, list: List<String>)
    fun loadCategory(key: String): List<String>
}

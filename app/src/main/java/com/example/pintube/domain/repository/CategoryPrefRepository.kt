package com.example.pintube.domain.repository

interface CategoryPrefRepository {
    fun saveCategoryList(categoryList: List<String>)
    fun getCategoryList(): List<String>
}

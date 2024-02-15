package com.example.pintube.data.repository.local

import android.util.Log
import com.example.pintube.data.local.dao.CategoryDAO
import com.example.pintube.data.local.dao.FavoriteDAO
import com.example.pintube.data.local.entity.CategoryEntity
import com.example.pintube.data.local.entity.FavoriteCategoryCross
import com.example.pintube.data.local.entity.FavoriteEntity
import com.example.pintube.domain.repository.LocalFavoriteRepository
import javax.inject.Inject

class LocalFavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDAO,
    private val categoryDao: CategoryDAO,
) : LocalFavoriteRepository {
    override suspend fun addBookmark(videoId: String, category: String) {
        var defaultCategoryId = categoryDao.getCategoryByName(category)?.categoryId
            ?: categoryDao.insert(CategoryEntity(name = category))
        if (defaultCategoryId !is Long) defaultCategoryId =
            categoryDao.getCategoryByName(category)!!.categoryId
        favoriteDao.insert(FavoriteEntity(videoId))
        favoriteDao.addFavoriteCategoryCross(FavoriteCategoryCross(videoId, defaultCategoryId))
    }

    override suspend fun checkIsBookmark(videoId: String): Boolean {
        return favoriteDao.checkBookmarkVideo(videoId)?.isBookmark ?: false
    }

    override suspend fun deleteBookmark(videoId: String) {
        favoriteDao.delete(FavoriteEntity(videoId = videoId))
    }

    override suspend fun addCategoryToFavorite(videoId: String, category: String) {
        var categoryId = categoryDao.getCategoryByName(category)?.categoryId
            ?: categoryDao.insert(CategoryEntity(name = category))
        if (categoryId !is Long) categoryId = categoryDao.getCategoryByName(category)!!.categoryId
        favoriteDao.addFavoriteCategoryCross(FavoriteCategoryCross(videoId, categoryId))
    }

    override suspend fun removeCategoryToFavorite(videoId: String, category: String) {
        var categoryId = categoryDao.getCategoryByName(category)!!.categoryId
        favoriteDao.deleteFavoriteCategoryCross(FavoriteCategoryCross(videoId, categoryId))
    }

    override suspend fun findCategoryVideos(category: String): List<String>? {
        var categoryId = categoryDao.getCategoryByName(category)?.categoryId
        return categoryId?.let { favoriteDao.getFavoritesByCategory(it).map { it.videoId } }
    }

}
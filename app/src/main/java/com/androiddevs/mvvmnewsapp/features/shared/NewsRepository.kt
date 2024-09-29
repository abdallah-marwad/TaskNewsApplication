package com.androiddevs.mvvmnewsapp.features.shared

import com.androiddevs.mvvmnewsapp.data.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.data.db.ArticleDao
import com.androiddevs.mvvmnewsapp.data.models.article.Article
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val articleDao : ArticleDao
) {


    suspend fun getBreakingNews(pageNumber : Int) =
        RetrofitInstance.api.getTopHeadLines(pageNumber = pageNumber)
    suspend fun getAllNews(pageNumber : Int) =
        RetrofitInstance.api.getAllNews(pageNumber = pageNumber)

    suspend fun getSearchingNews(searchQuery: String , pageNumber : Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = articleDao.upsert(article)

    suspend fun delete(article: Article) = articleDao.deleteArticle(article)

    fun getSavedNews() = articleDao.getAllArticles()
}
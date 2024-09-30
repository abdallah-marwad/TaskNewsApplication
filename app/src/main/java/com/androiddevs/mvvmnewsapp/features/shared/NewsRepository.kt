package com.androiddevs.mvvmnewsapp.features.shared

import com.androiddevs.mvvmnewsapp.data.api.RetrofitInstance
import com.androiddevs.mvvmnewsapp.data.db.ArticleDao
import com.androiddevs.mvvmnewsapp.data.db.UserDao
import com.androiddevs.mvvmnewsapp.data.models.article.Article
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val articleDao : ArticleDao ,
    private val userDao : UserDao ,
) {


    suspend fun getBreakingNews(pageNumber : Int) =
        RetrofitInstance.api.getTopHeadLines(pageNumber = pageNumber)
    suspend fun getAllNews(pageNumber : Int) =
        RetrofitInstance.api.getAllNews(pageNumber = pageNumber)

    suspend fun getSearchingNews(searchQuery: String , pageNumber : Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

     fun upsert(article: Article) = articleDao.upsert(article)

    suspend fun delete(article: Article) = articleDao.deleteArticle(article)

    fun getSavedNews() = articleDao.getAllArticles()
    fun getArticlesByIds(articleIds: List<Int>) = articleDao.getArticlesByIds(articleIds)

    fun getFavList(userId : Int) = userDao.getFavList(userId)
    fun updateFavArticles(userId : Int , favList : List<Int>) = userDao.updateFavArticles(userId , favList)
}
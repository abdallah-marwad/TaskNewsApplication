package com.androiddevs.mvvmnewsapp.data.api

import com.androiddevs.mvvmnewsapp.data.models.article.NewsResponse
import com.androiddevs.mvvmnewsapp.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getTopHeadLines(
        @Query("category")
        category:String = "technology",
        @Query("page")
        pageNumber:Int =1 ,
        @Query("apiKey")
        apiKey:String = API_KEY,
    ): Response<NewsResponse>
    @GET("v2/everything")
    suspend fun getAllNews(
        @Query("sources")
        category:String = "wired",
        @Query("page")
        pageNumber:Int =1 ,
        @Query("apiKey")
        apiKey:String = API_KEY,
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q")
        searchQuery:String ,
        @Query("page")
        pageNumber:Int =1 ,
        @Query("apiKey")
        apiKey:String = API_KEY
    ): Response<NewsResponse>
}
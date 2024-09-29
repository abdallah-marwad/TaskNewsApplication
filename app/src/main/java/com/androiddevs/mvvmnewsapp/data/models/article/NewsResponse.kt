package com.androiddevs.mvvmnewsapp.data.models.article

data class NewsResponse(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)
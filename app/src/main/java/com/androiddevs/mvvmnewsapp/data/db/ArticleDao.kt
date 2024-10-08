package com.androiddevs.mvvmnewsapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import com.androiddevs.mvvmnewsapp.data.models.article.Article

@Dao
interface ArticleDao {


    @Insert(onConflict = REPLACE)
     fun upsert(article: Article) : Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

    @Query("SELECT * FROM articles WHERE id IN (:articleIds)")
    fun getArticlesByIds(articleIds: List<Int>): LiveData<List<Article>?>

}
package com.androiddevs.mvvmnewsapp.di

import android.content.Context
import androidx.room.Room
import com.androiddevs.mvvmnewsapp.data.db.ArticleDao
import com.androiddevs.mvvmnewsapp.data.db.ArticleDatabase
import com.androiddevs.mvvmnewsapp.data.db.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ArticleDatabase {
        return ArticleDatabase.invoke(context)
    }


    @Provides
    fun provideArticleDao(database: ArticleDatabase): ArticleDao {
        return database.getArticleDao()
    }
   @Provides
    fun provideUserDao(database: ArticleDatabase): UserDao {
        return database.getUserDao()
    }

}
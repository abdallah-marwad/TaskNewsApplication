package com.androiddevs.mvvmnewsapp.features.shared

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.data.models.article.Article
import com.androiddevs.mvvmnewsapp.data.models.article.NewsResponse
import com.androiddevs.mvvmnewsapp.utils.Resource
import com.tailors.doctoria.utils.InternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    application: Application
) : AndroidViewModel(application) {
    init {
        getBreakingNews()
    }
    // Breaking News Handling
    val breakingNews = MutableLiveData<Resource<NewsResponse>>()
    val topBreakingNews = MutableLiveData<Resource<NewsResponse>>()
    var breakingNewsPage = 1
    var breakingNewsResponse : NewsResponse? = null
    var topBreakingNewsResponse : NewsResponse? = null


     fun getBreakingNews() =
        viewModelScope.launch(Dispatchers.IO)
        {
            launch { safeBreakingNewsCall()}
            launch {  safeTopHeadNewsCall()}
        }

    /**
    * @param response the async Api Call by ViewModelScope
    * @return Resource that carry the state with the data
    * */
    private fun handleBreakingNewResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                breakingNewsPage++
                if(breakingNewsResponse == null){
                    breakingNewsResponse = it
                }
                else{
                    val oldArticle = breakingNewsResponse?.articles
                    val newArticle = it.articles
                    oldArticle?.addAll(newArticle)

                }
                return Resource.Success(breakingNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeBreakingNewsCall() {
        breakingNews.postValue(Resource.Loading())
        try {
                val response = newsRepository.getAllNews(breakingNewsPage)
                breakingNews.postValue(handleBreakingNewResponse(response))
        } catch (t: Throwable) {
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }


    }
    private suspend fun safeTopHeadNewsCall() {
        topBreakingNews.postValue(Resource.Loading())
        try {
                val response = newsRepository.getBreakingNews(breakingNewsPage)
                topBreakingNews.postValue(handleTopBreakingNewResponse(response))
//
        } catch (t: Throwable) {
            when(t){
                is IOException -> topBreakingNews.postValue(Resource.Error("Network Failure"))
                else -> topBreakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    private fun handleTopBreakingNewResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                if(topBreakingNewsResponse == null){
                    topBreakingNewsResponse = it
                }
                else{
                    val oldArticle = topBreakingNewsResponse?.articles
                    val newArticle = it.articles
                    oldArticle?.addAll(newArticle)

                }
                return Resource.Success(topBreakingNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }
    //Room Operations
    val addStateFlow = MutableStateFlow<Long>(0)
    val addedArticle = MutableStateFlow<Long>(0)
     fun saveArticle(article: Article) {
         viewModelScope.launch(Dispatchers.IO) {
             addedArticle.emit(newsRepository.upsert(article))
         }
     }

    fun deleteArticle(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.delete(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()
    fun getFavList(userId : Int) = newsRepository.getFavList(userId)
    fun getArticlesByIds(favIds :  List<Int>) = newsRepository.getArticlesByIds(favIds)
    fun updateFavArticles(userId : Int , favList : List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            newsRepository.updateFavArticles(userId, favList)
        }
    }



}
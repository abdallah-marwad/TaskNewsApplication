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
    var breakingNewsPage = 1
    var breakingNewsResponse : NewsResponse? = null


     fun getBreakingNews() =
        viewModelScope.launch(Dispatchers.IO)
        {
            safeBreakingNewsCall()
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
            if (InternetConnection().hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(breakingNewsPage)
                breakingNews.postValue(handleBreakingNewResponse(response))
            } else {
                breakingNews.postValue(Resource.Error("No Internet Connection"))

            }
        } catch (t: Throwable) {
            when(t){
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }
        }


    }
    //Room Operations
    val addStateFlow = MutableStateFlow<Long>(0)
    fun saveArticle(article: Article) =
        viewModelScope.launch(Dispatchers.IO) {

            addStateFlow.emit(newsRepository.upsert(article))

        }

    fun deleteArticle(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        newsRepository.delete(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()



}
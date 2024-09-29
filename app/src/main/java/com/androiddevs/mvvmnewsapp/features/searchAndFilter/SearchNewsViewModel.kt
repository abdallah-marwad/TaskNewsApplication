package com.androiddevs.mvvmnewsapp.features.searchAndFilter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.androiddevs.mvvmnewsapp.data.models.article.NewsResponse
import com.androiddevs.mvvmnewsapp.features.shared.NewsRepository
import com.androiddevs.mvvmnewsapp.utils.Resource
import com.tailors.doctoria.application.core.BaseViewModel
import com.tailors.doctoria.utils.InternetConnection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class SearchNewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
): BaseViewModel() {
    // Search News Handling
    val searchingNews = MutableLiveData<Resource<NewsResponse>>()
    var searchingNewsPage = 1
    var searchNewsResponse : NewsResponse? = null

    fun getSearchingNews(searchQuery: String) = viewModelScope.launch(Dispatchers.IO) {
        safeSearchingNewsCall(searchQuery)

    }
    private suspend fun safeSearchingNewsCall(searchQuery: String) {
        searchingNews.postValue(Resource.Loading())
        try {
            if (InternetConnection().hasInternetConnection()) {
                val response = newsRepository.getSearchingNews(searchQuery, searchingNewsPage)
                searchingNews.postValue(handleSearchingNewsResponse(response))
            } else {
                searchingNews.postValue(Resource.Error("No Internet Connection"))

            }
        } catch (t: Throwable) {
            when(t){
                is IOException -> searchingNews.postValue(Resource.Error("Network Failure"))
                else -> searchingNews.postValue(Resource.Error("Conversion Error"))
            }
        }


    }

    private fun handleSearchingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                searchingNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = it
                } else {
                    val oldArticle = searchNewsResponse?.articles
                    val newArticle = it.articles
                    oldArticle?.addAll(newArticle)

                }
                return Resource.Success(searchNewsResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }

}
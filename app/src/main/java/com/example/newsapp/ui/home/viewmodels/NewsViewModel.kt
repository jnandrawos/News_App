package com.example.newsapp.ui.home.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.common.UtilityFunctions
import com.example.newsapp.util.NewsApplication
import com.example.newsapp.data.models.NewsResponseModel
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.data.repository.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository,
    application: Application,
) :
    AndroidViewModel(application), Observable {

    var newsPeriod = 1
    val mostViewedNews: MutableLiveData<Resource<NewsResponseModel>> = MutableLiveData()

    init {
        getMostViewedNews()
    }


    fun getMostViewedNews() = viewModelScope.launch {
        if(UtilityFunctions.hasInternetConnection(getApplication<NewsApplication>())) {
            mostViewedNews.postValue(Resource.Loading())
            val response = repository.getMostViewedNews(newsPeriod)
            mostViewedNews.postValue(handleMostViewedNewsResponse(response))
        }
    }

    private fun handleMostViewedNewsResponse(response: Response<NewsResponseModel>): Resource<NewsResponseModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message(), null)
    }


    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

}
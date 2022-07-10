package com.example.newsapp.home.data.repository

import com.example.newsapp.home.data.remote.RetrofitInstance
import javax.inject.Inject

class NewsRepository @Inject constructor(){

    suspend fun getMostViewedNews(period: Int)=
        RetrofitInstance.api.getMostViewedNews(period)


}
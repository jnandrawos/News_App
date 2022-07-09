package com.example.newsapp.repository

import com.example.newsapp.api.RetrofitInstance
import javax.inject.Inject

class NewsRepository @Inject constructor(){

    suspend fun getMostViewedNews(period: Int)=
        RetrofitInstance.api.getMostViewedNews(period)


}
package com.example.newsapp.data.repository

import com.example.newsapp.data.remote.NewsAPI
import com.example.newsapp.data.remote.RetrofitInstance
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsAPI: NewsAPI){

    suspend fun getMostViewedNews(period: Int)=
        newsAPI.getMostViewedNews(period)


}
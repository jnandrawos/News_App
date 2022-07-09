package com.example.newsapp.api

import com.example.newsapp.entities.NewsResponse
import com.example.newsapp.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsAPI {

    @GET("svc/mostpopular/v2/mostviewed/all-sections/{period}.json")
    suspend fun getMostViewedNews(
        @Path("period")
        period: Int,
        @Query("api-key")
        apiKey: String = Constants.API_KEY
    ):Response<NewsResponse>

}
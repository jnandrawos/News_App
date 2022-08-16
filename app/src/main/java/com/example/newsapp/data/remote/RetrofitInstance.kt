package com.example.newsapp.data.remote

import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RetrofitInstance {

    val client = OkHttpClient.Builder()
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): NewsAPI = retrofit.create(NewsAPI::class.java)

    @Singleton
    @Provides
    fun providesRepository(newsAPI: NewsAPI) = NewsRepository(newsAPI)


}
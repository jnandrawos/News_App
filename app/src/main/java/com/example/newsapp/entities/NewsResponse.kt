package com.example.newsapp.entities

import com.google.gson.annotations.SerializedName


data class NewsResponse(

    @SerializedName("status") var status: String? = null,
    @SerializedName("copyright") var copyright: String? = null,
    @SerializedName("num_results") var numResults: Int? = null,
    @SerializedName("results") var results: MutableList<Article>,

    )
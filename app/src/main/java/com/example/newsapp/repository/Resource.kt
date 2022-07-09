package com.example.newsapp.repository

sealed class Resource<T>(val data: T? = null, val message: String? = null) {

    class Success<T>(data: T): Resource<T>(data)

    class Loading<T>(): Resource<T>()

    class Error<T>(message: String, data: T?): Resource<T>(data, message)

}
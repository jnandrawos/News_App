package com.example.newsapp.repository

import com.example.newsapp.database.UserDao
import com.example.newsapp.database.UserEntity
import javax.inject.Inject


class UserRepository @Inject constructor(private val dao: UserDao) {

    val users = dao.getAllUsers()

    suspend fun insert(user: UserEntity) {
        return dao.insert(user)
    }

    suspend fun update(user: UserEntity){
        return dao.update(user)
    }


    suspend fun getUser(email: String): UserEntity?{
        return dao.getUser(email)
    }




}
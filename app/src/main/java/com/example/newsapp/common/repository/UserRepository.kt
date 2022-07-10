package com.example.newsapp.common.repository

import com.example.newsapp.common.database.UserDao
import com.example.newsapp.common.database.UserEntity
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
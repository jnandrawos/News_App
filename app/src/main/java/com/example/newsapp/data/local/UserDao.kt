package com.example.newsapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: UserEntity)

    @Update
    suspend fun update(user: UserEntity)

    @Query("SELECT * FROM users_table")
    fun getAllUsers(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM users_table WHERE email LIKE :email")
    suspend fun getUser(email: String): UserEntity?


}
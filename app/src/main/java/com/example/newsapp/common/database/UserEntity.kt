package com.example.newsapp.common.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users_table")
data class UserEntity(

    @PrimaryKey
    var email: String,

    @ColumnInfo(name = "password")
    var password: String,

    @ColumnInfo(name = "name")
    var name: String,
    )

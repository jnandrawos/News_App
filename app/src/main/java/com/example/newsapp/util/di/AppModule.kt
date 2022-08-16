package com.example.newsapp.util.di

import android.content.Context
import androidx.room.Room
import com.example.newsapp.data.local.UserDatabase
import com.example.newsapp.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        UserDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Provides
    @Singleton
    fun provideProfileDao(db: UserDatabase) = db.userDao()

}
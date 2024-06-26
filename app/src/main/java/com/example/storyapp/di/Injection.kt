package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository = runBlocking {
    val pref = UserPreference.getInstance(context.dataStore)
        val user = pref.getSession().first()
        val apiService = ApiConfig.getApiService(user.token)
        UserRepository.getInstance(apiService, pref)
    }
}
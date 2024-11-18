package com.example.storyapp.inject

import android.content.Context
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.preference.UserPreference
import com.example.storyapp.preference.dataStore
import com.example.storyapp.repository.StoryRepository

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)

        val apiService = ApiConfig.getApiService()

        return StoryRepository.getInstance(apiService, pref)
    }

}
package com.example.storyapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.storyapp.data.response.StoriesResponse
import com.example.storyapp.repository.StoryRepository
import com.example.storyapp.utils.Result as StoryResult // Typealias

class MapsViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun getStoriesWithLocation(): LiveData<StoryResult<StoriesResponse>> = liveData {
        emit(StoryResult.Loading)
        try {
            val response = storyRepository.getStoriesWithLocation()
            emit(StoryResult.Success(response))
        } catch (e: Exception) {
            emit(StoryResult.Error(e.message ?: "An error occurred"))
        }
    }
}
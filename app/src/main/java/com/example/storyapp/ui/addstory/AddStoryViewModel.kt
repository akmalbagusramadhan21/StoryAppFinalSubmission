package com.example.storyapp.ui.addstory

import androidx.lifecycle.ViewModel
import com.example.storyapp.repository.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel (private val repository: StoryRepository) :ViewModel() {
    fun addStory(file : MultipartBody.Part, description: RequestBody) = repository.addStory(file,description)
}
package com.example.storyapp.ui.detailstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.response.Story
import com.example.storyapp.repository.StoryRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val _detailStory = MutableLiveData<Story?>()
    val detailStory: LiveData<Story?> = _detailStory

    fun getDetailStory(id: String) {
        viewModelScope.launch {
            val response = storyRepository.getDetailStory(id)
            _detailStory.postValue(response?.story)
        }
    }
}
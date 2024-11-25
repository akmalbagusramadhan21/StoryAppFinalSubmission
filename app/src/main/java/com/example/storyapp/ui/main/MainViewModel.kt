package com.example.storyapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.response.ListStoryItem
import com.example.storyapp.repository.StoryRepository
import kotlinx.coroutines.flow.Flow

class MainViewModel(
    private val storyRepository: StoryRepository,
) : ViewModel() {



//    private val _stories = MutableLiveData<List<ListStoryItem>>()
    val stories: Flow<PagingData<ListStoryItem>> =
        storyRepository.getStoryPagingSource().cachedIn(viewModelScope)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isLoggedOut = MutableLiveData<Boolean>()
    val isLoggedOut: LiveData<Boolean> = _isLoggedOut


//    init {
//        viewModelScope.launch {
//            fetchStories()
//        }
//    }

    suspend fun logout() {
        storyRepository.logout()
        _isLoggedOut.postValue(true)
    }


//    private fun fetchStories(){
//        _isLoading.value = true
//        viewModelScope.launch {
//            try {
//                val response = storyRepository.getStoryPagingSource(location = 1)
//                _stories.value = response.listStory
//
//            } catch (e: HttpException) {
//                Log.e("MainViewModel", "Network error: ${e.localizedMessage}")
//            } catch (e: IOException) {
//                Log.e("MainViewModel", "Connection error: ${e.localizedMessage}")
//            } catch (e: Exception) {
//                Log.e("MainViewModel", "An unexpected error occurred: ${e.localizedMessage}", e)
//            } finally {
//                _isLoading.value = false
//            }
//        }
    }

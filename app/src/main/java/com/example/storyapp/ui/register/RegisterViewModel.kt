package com.example.storyapp.ui.register

import android.util.Log
import androidx.datastore.core.IOException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.request.RegisterRequest
import com.example.storyapp.repository.StoryRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun register(registerRequest: RegisterRequest, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                if (registerRequest.name.isBlank() || registerRequest.email.isBlank() || registerRequest.password.isBlank()) {
                    onError("Please fill in all fields")
                    return@launch
                }
                val response = storyRepository.register(
                    registerRequest.name,
                    registerRequest.email,
                    registerRequest.password
                )
                if (response.error == false) {
                    onSuccess()
                } else {
                    onError(response.message ?: "Registration failed")
                }
            } catch (e: HttpException) {
                onError("Network error: ${e.localizedMessage}")
            } catch (e: IOException) {
                onError("Connection error: ${e.localizedMessage}")
            } catch (e: Exception) {
                onError("An unexpected error occurred: ${e.localizedMessage}")
                Log.e("RegisterViewModel", "Registration error", e)
            }
        }
    }
}

package com.example.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.request.LoginRequest
import com.example.storyapp.preference.UserModel
import com.example.storyapp.repository.StoryRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    private val _session = MutableLiveData<UserModel>()
    val storySession: LiveData<UserModel> get() = _session

    fun login(loginRequest: LoginRequest, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val response = storyRepository.login(loginRequest)

                if (response.loginResult?.token.isNullOrEmpty()) {
                    onError("Password salah boss")
                    return@launch
                }

                val user = UserModel(
                    email = loginRequest.email,
                    token = response.loginResult?.token ?: "",
                    isLogin = true
                )
                storyRepository.saveSession(user)
                _session.value = user
                onSuccess()

            } catch (e: Exception) {
                onError(e.message ?: "Login failed")
            }
        }
    }

    fun getSession(): LiveData<UserModel> = storyRepository.getSession().asLiveData()
}
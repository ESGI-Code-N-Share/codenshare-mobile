package com.example.code_n_share_mobile.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_n_share_mobile.models.auth.LoginRequest
import com.example.code_n_share_mobile.models.auth.RegisterRequest
import com.example.code_n_share_mobile.repositories.UserRepository
import kotlinx.coroutines.launch

data class RegistrationResult(val success: Boolean, val error: String?)

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _registrationResult = MutableLiveData<RegistrationResult>()
    val registrationResult: LiveData<RegistrationResult> = _registrationResult

    fun registerUser(firstname: String, lastname: String, email: String, password: String, birthdate: String) {
        viewModelScope.launch {
            try {
                val request = RegisterRequest(firstname, lastname, email, password, birthdate)
                val response = userRepository.registerUser(request)
                Log.d("AuthViewModel", "Register response: ${response.data}")
                _registrationResult.postValue(RegistrationResult(success = true, error = null))
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Register error: ${e.message}")
                _registrationResult.postValue(RegistrationResult(success = false, error = e.message))
            }
        }
    }

    fun loginUser(email: String, password: String, stayLogin: Boolean) {
        viewModelScope.launch {
            try {
                val request = LoginRequest(email, password, stayLogin)
                val response = userRepository.loginUser(request)
                Log.d("AuthViewModel", "Login response: ${response.data.token}")
                // Handle success
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Login error: ${e.message}")
                // Handle error
            }
        }
    }

    fun verifyEmail(userId: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.verifyEmail(userId)
                Log.d("AuthViewModel", "Verify email response: ${response.message}")
                // Handle success
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Verify email error: ${e.message}")
                // Handle error
            }
        }
    }
}

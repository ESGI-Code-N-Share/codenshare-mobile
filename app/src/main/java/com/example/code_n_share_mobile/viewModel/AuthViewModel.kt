// AuthViewModel.kt
package com.example.code_n_share_mobile.viewModel

import LoginRequest
import RegisterRequest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_n_share_mobile.repositories.AuthRepository
import com.example.code_n_share_mobile.utils.extractErrorMessage
import com.example.code_n_share_mobile.view.EmailVerificationActivity
import com.example.code_n_share_mobile.view.MainActivity
import kotlinx.coroutines.launch

data class RegistrationResult(val success: Boolean, val error: String?)
data class LoginResult(val success: Boolean, val token: String?, val avatarUrl: String?, val error: String?)
data class LogoutResult(val success: Boolean, val error: String?)

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _registrationResult = MutableLiveData<RegistrationResult>()
    val registrationResult: LiveData<RegistrationResult> = _registrationResult

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _logoutResult = MutableLiveData<LogoutResult>()
    val logoutResult: LiveData<LogoutResult> = _logoutResult

    fun registerUser(context: Context, firstname: String, lastname: String, email: String, password: String, birthdate: String) {
        viewModelScope.launch {
            try {
                Log.d("AuthViewModel", "Attempting to register user with email: $email")
                val request = RegisterRequest(firstname, lastname, email, password, birthdate)
                val response = authRepository.registerUser(request)
                Log.d("AuthViewModel", "Registration successful for user with email: $email")
                _registrationResult.postValue(RegistrationResult(success = true, error = null))
                verifyEmail(context, response.data)
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                Log.e("AuthViewModel", "Register error: $errorMessage")
                showToast(context, errorMessage)
                _registrationResult.postValue(RegistrationResult(success = false, error = errorMessage))
            }
        }
    }

    private fun verifyEmail(context: Context, userId: String) {
        viewModelScope.launch {
            val intent = Intent(context, EmailVerificationActivity::class.java)
            try {
                intent.putExtra("success", true)
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Email verification error: ${e.message}")
                intent.putExtra("success", false)
            } finally {
                context.startActivity(intent)
            }
        }
    }

    fun loginUser(context: Context, email: String, password: String, stayLogin: Boolean) {
        viewModelScope.launch {
            try {
                Log.d("AuthViewModel", "Attempting login with email: $email")
                val request = LoginRequest(email, password, stayLogin)
                val response = authRepository.loginUser(request)

                val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("token", response.data.token)
                    putString("avatarUrl", response.data.avatar)
                    putString("userId", response.data.userId)
                    putString("firstname", response.data.firstname)
                    putString("lastname", response.data.lastname)
                    putString("email", response.data.email)
                    putString("overview", response.data.overview)
                    apply()
                }

                Log.d("AuthViewModel", "Login successful for user with email: $email")
                _loginResult.postValue(LoginResult(success = true, token = response.data.token, avatarUrl = response.data.avatar, error = null))

                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                Log.e("AuthViewModel", "Login error: $errorMessage")
                showToast(context, errorMessage)
                _loginResult.postValue(LoginResult(success = false, token = null, avatarUrl = null, error = errorMessage))
            }
        }
    }

    fun logoutUser(context: Context) {
        viewModelScope.launch {
            try {
                val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getString("userId", null)

                if (userId != null) {
                    Log.d("AuthViewModel", "Attempting logout for user ID: $userId")
                    authRepository.logoutUser(userId)

                    clearSharedPreferences(sharedPreferences)

                    Log.d("AuthViewModel", "Logout successful for user ID: $userId")
                    _logoutResult.postValue(LogoutResult(success = true, error = null))

                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                } else {
                    val errorMessage = "User ID not found"
                    Log.e("AuthViewModel", "Logout error: $errorMessage")
                    showToast(context, errorMessage)
                    _logoutResult.postValue(LogoutResult(success = false, error = errorMessage))
                }
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                Log.e("AuthViewModel", "Logout error: $errorMessage")
                showToast(context, errorMessage)
                _logoutResult.postValue(LogoutResult(success = false, error = errorMessage))
            }
        }
    }

    private fun clearSharedPreferences(sharedPreferences: SharedPreferences) {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }

    private fun showToast(context: Context, message: String?) {
        Toast.makeText(context, message ?: "Unknown error occurred", Toast.LENGTH_SHORT).show()
    }
}

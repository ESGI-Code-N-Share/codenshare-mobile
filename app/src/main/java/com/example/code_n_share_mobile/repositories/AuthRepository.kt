package com.example.code_n_share_mobile.repositories

import LoginRequest
import LoginResponse
import LogoutResponse
import RegisterRequest
import RegisterResponse
import android.util.Log
import com.example.code_n_share_mobile.network.AuthApiService

class AuthRepository(private val authApiService: AuthApiService) {


    suspend fun registerUser(request: RegisterRequest): RegisterResponse {
        Log.d("AuthRepository", "Sending register request: $request")
        try {
            val response = authApiService.registerUser(request)
            Log.d("AuthRepository", "Received register response: $response")
            return response
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error registering user: ${e.message}")
            throw e
        }
    }

    suspend fun loginUser(request: LoginRequest): LoginResponse {
        return authApiService.loginUser(request)
    }

    suspend fun logoutUser(userId: String): LogoutResponse {
        return authApiService.logoutUser(userId)
    }
}
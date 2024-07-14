package com.example.code_n_share_mobile.repositories

import com.example.code_n_share_mobile.models.auth.LoginRequest
import com.example.code_n_share_mobile.models.auth.LoginResponse
import com.example.code_n_share_mobile.models.auth.LogoutResponse
import com.example.code_n_share_mobile.models.auth.RegisterRequest
import com.example.code_n_share_mobile.models.auth.RegisterResponse
import com.example.code_n_share_mobile.network.AuthApiService

class AuthRepository(private val authApiService: AuthApiService) {

    suspend fun registerUser(request: RegisterRequest): RegisterResponse {
        return authApiService.registerUser(request)
    }

    suspend fun loginUser(request: LoginRequest): LoginResponse {
        return authApiService.loginUser(request)
    }

    suspend fun logoutUser(userId: String): LogoutResponse {
        return authApiService.logoutUser(userId)
    }
}
package com.example.code_n_share_mobile.repositories

import com.example.code_n_share_mobile.models.auth.LoginRequest
import com.example.code_n_share_mobile.models.auth.RegisterRequest
import com.example.code_n_share_mobile.network.AuthApiService

class UserRepository(private val authApiService: AuthApiService) {

    suspend fun registerUser(request: RegisterRequest) = authApiService.registerUser(request)

    suspend fun loginUser(request: LoginRequest) = authApiService.loginUser(request)

    suspend fun verifyEmail(userId: String) = authApiService.verifyEmail(userId)
}

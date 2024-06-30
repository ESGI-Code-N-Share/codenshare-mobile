package com.example.code_n_share_mobile.network

import com.example.code_n_share_mobile.models.auth.LoginRequest
import com.example.code_n_share_mobile.models.auth.LoginResponse
import com.example.code_n_share_mobile.models.auth.RegisterRequest
import com.example.code_n_share_mobile.models.auth.RegisterResponse
import com.example.code_n_share_mobile.models.auth.VerifyEmailResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
interface AuthApiService {
    @POST("/api/v1/register")
    suspend fun registerUser(@Body request: RegisterRequest): RegisterResponse

    @POST("/api/v1/login")
    suspend fun loginUser(@Body request: LoginRequest): LoginResponse

    @GET("/api/v1/verify-email/{id}")
    suspend fun verifyEmail(@Path("id") userId: String): VerifyEmailResponse
}
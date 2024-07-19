package com.example.code_n_share_mobile.network


import LoginRequest
import LoginResponse
import LogoutResponse
import RegisterRequest
import RegisterResponse
import VerifyEmailResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApiService {
    @POST("/api/v1/register")
    suspend fun registerUser(@Body request: RegisterRequest): RegisterResponse

    @POST("/api/v1/login")
    suspend fun loginUser(@Body request: LoginRequest): LoginResponse

    @GET("/api/v1/verify-email/{id}")
    suspend fun verifyEmail(@Path("id") userId: String): VerifyEmailResponse

    @POST("/api/v1/logout")
    suspend fun logoutUser(@Query("userId") userId: String): LogoutResponse
}
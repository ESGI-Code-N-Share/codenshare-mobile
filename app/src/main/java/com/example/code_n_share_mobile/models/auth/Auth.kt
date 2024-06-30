package com.example.code_n_share_mobile.models.auth

import com.example.code_n_share_mobile.models.User

data class RegisterRequest(
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
    val birthdate: String
)

data class RegisterResponse(
    val data: String
)

data class LoginRequest(
    val email: String,
    val password: String,
    val stayLogin: Boolean
)

data class LoginResponse(
    val data: User
)

data class VerifyEmailResponse(
    val message: String
)
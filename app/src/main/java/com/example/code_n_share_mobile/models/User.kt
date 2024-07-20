package com.example.code_n_share_mobile.models

data class User(
    val userId: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val token: String,
    val avatar: String,
    val overview: String?
)

data class EditUser(
    val firstname: String,
    val lastname: String,
    val overview: String?
)

data class UserResponse(
    val data: List<User>
)



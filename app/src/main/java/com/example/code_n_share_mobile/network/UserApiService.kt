package com.example.code_n_share_mobile.network

import com.example.code_n_share_mobile.models.User
import com.example.code_n_share_mobile.models.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {

    @GET("/api/v1/users/search")
    suspend fun searchUsers(@Query("query") query: String): UserResponse

    @POST("/api/v1/friends")
    suspend fun followUser(@Body requestBody: Map<String, String>)

    @HTTP(method = "DELETE", path = "/api/v1/friends", hasBody = true)
    suspend fun unfollowUser(@Body requestBody: Map<String, String>)

    @GET("/api/v1/friends/following")
    suspend fun getFollowingByUser(@Query("userId") userId: String): UserResponse

    @GET("/api/v1/friends/followers")
    suspend fun getFollowers(@Query("userId") userId: String): UserResponse

    @GET("/api/v1/users/{userId}")
    suspend fun getUserById(@Path("userId") userId: String): User
}

package com.example.code_n_share_mobile.network

import com.example.code_n_share_mobile.models.CreatePost
import com.example.code_n_share_mobile.models.PostResponse
import com.example.code_n_share_mobile.models.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApiService {
    @GET("/api/v1/posts")
    suspend fun getPosts(): PostResponse

    @POST("/api/v1/posts")
    suspend fun createPost(@Body post: CreatePost)

    @HTTP(method = "DELETE", path = "/api/v1/posts/{postId}", hasBody = true)
    suspend fun deletePost(@Path("postId") postId: String, @Body requestBody: Map<String, String>)

    @GET("/api/v1/users/{userId}")
    suspend fun getUserById(@Path("userId") userId: String): User

    @POST("/api/v1/posts/{postId}/likes")
    suspend fun likePost(@Path("postId") postId: String, @Body userId: Map<String, String>)

    @HTTP(method = "DELETE", path = "/api/v1/posts/{postId}/likes", hasBody = true)
    suspend fun unlikePost(@Path("postId") postId: String, @Body userId: Map<String, String>)
}

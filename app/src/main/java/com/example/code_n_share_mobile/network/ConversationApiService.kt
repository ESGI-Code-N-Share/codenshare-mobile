package com.example.code_n_share_mobile.network

import ConversationResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ConversationApiService {
    @GET("/api/v1/users/{userId}/conversations")
    suspend fun getConversations(@Path("userId") userId: String): ConversationResponse
}

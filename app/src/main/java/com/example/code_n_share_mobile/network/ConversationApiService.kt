package com.example.code_n_share_mobile.network

import Conversation
import ConversationResponse
import CreateConversationRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path

interface ConversationApiService {
    @GET("/api/v1/users/{userId}/conversations")
    suspend fun getConversations(@Path("userId") userId: String): ConversationResponse

    @POST("/api/v1/users/{userId}/conversations")
    suspend fun createConversation(@Path("userId") userId: String, @Body request: CreateConversationRequest): Conversation

    @HTTP(method = "DELETE", path = "/api/v1/users/{userId}/conversations/{conversationId}", hasBody = true)
    suspend fun deleteConversation(@Path("userId") userId: String, @Path("conversationId") conversationId: String, @Body requestBody: Map<String, String>): Conversation
}

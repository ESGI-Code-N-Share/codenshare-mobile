package com.example.code_n_share_mobile.network

import com.example.code_n_share_mobile.models.Message
import com.example.code_n_share_mobile.models.MessageResponse
import com.example.code_n_share_mobile.models.SendMessageRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessageApiService {

    @GET("/api/v1/users/{userId}/conversations/{conversationId}/messages")
    suspend fun getMessages(@Path("userId") userId: String, @Path("conversationId") conversationId: String): MessageResponse

    @POST("/api/v1/users/{userId}/conversations/{conversationId}/messages")
    suspend fun sendMessage(@Path("userId") userId: String, @Path("conversationId") conversationId: String, @Body request: SendMessageRequest): Message
}

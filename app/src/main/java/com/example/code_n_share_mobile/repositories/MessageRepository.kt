package com.example.code_n_share_mobile.repositories

import android.util.Log
import com.example.code_n_share_mobile.models.Message
import com.example.code_n_share_mobile.models.SendMessageRequest
import com.example.code_n_share_mobile.network.MessageApiService

class MessageRepository(private val messageApiService: MessageApiService) {

    suspend fun getMessages(userId: String, conversationId: String): List<Message> {
        return try {
            val response = messageApiService.getMessages(userId, conversationId)
            response.data
        } catch (e: Exception) {
            Log.e("MessageRepository", "Error fetching messages: ${e.message}")
            emptyList()
        }
    }

    suspend fun sendMessage(userId: String, conversationId: String, request: SendMessageRequest): Message? {
        return try {
            messageApiService.sendMessage(userId, conversationId, request)
        } catch (e: Exception) {
            Log.e("MessageRepository", "Error sending message: ${e.message}")
            null
        }
    }
}

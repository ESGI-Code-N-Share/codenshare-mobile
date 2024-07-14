package com.example.code_n_share_mobile.repositories

import Conversation
import CreateConversationRequest
import android.util.Log
import com.example.code_n_share_mobile.network.ConversationApiService

class ConversationRepository(private val conversationApiService: ConversationApiService) {

    suspend fun getConversations(userId: String): List<Conversation> {
        return try {
            val response = conversationApiService.getConversations(userId)
            response.data
        } catch (e: Exception) {
            Log.e("ConversationRepository", "Error fetching conversations: ${e.message}")
            emptyList()
        }
    }

    suspend fun createConversation(ownerId: String, memberIds: List<String>): Conversation? {
        return try {
            val request = CreateConversationRequest(ownerId, memberIds)
            val conversation = conversationApiService.createConversation(ownerId, request)
            conversation
        } catch (e: Exception) {
            Log.e("ConversationRepository", "Error creating conversation: ${e.message}")
            null
        }
    }

    suspend fun deleteConversation(userId: String, conversationId: String): Conversation? {
        return try {
            val requestBody = mapOf("userId" to userId, "conversationId" to conversationId)
            conversationApiService.deleteConversation(userId, conversationId, requestBody)
        } catch (e: Exception) {
            Log.e("ConversationRepository", "Error deleting conversation: ${e.message}")
            null
        }
    }
}

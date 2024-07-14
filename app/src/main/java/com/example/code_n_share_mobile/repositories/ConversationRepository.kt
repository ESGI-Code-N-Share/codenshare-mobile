package com.example.code_n_share_mobile.repositories

import Conversation
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
}

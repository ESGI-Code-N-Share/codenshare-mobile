package com.example.code_n_share_mobile.viewModel

import Conversation
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_n_share_mobile.repositories.ConversationRepository
import kotlinx.coroutines.launch

class ConversationViewModel(private val conversationRepository: ConversationRepository) : ViewModel() {

    private val _conversations = MutableLiveData<List<Conversation>>()
    val conversations: LiveData<List<Conversation>> get() = _conversations

    private val _creationResult = MutableLiveData<Conversation?>()
    val creationResult: MutableLiveData<Conversation?> get() = _creationResult

    fun loadConversations(userId: String) {
        viewModelScope.launch {
            try {
                val conversationList = conversationRepository.getConversations(userId)
                _conversations.postValue(conversationList)
            } catch (e: Exception) {
                Log.e("ConversationViewModel", "Error loading conversations: ${e.message}")
            }
        }
    }

    fun createConversation(ownerId: String, memberIds: List<String>) {
        viewModelScope.launch {
            try {
                val newConversation = conversationRepository.createConversation(ownerId, memberIds)
                _creationResult.postValue(newConversation)
                loadConversations(ownerId)
            } catch (e: Exception) {
                Log.e("ConversationViewModel", "Error creating conversation: ${e.message}")
            }
        }
    }

    fun deleteConversation(userId: String, conversationId: String) {
        viewModelScope.launch {
            try {
                val deletedConversation = conversationRepository.deleteConversation(userId, conversationId)
                if (deletedConversation != null) {
                    loadConversations(userId)
                }
            } catch (e: Exception) {
                Log.e("ConversationViewModel", "Error deleting conversation: ${e.message}")
            }
        }
    }
}

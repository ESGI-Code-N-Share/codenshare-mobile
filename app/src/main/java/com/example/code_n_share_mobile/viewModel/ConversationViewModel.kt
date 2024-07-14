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

    fun loadConversations(userId: String) {
        viewModelScope.launch {
            try {
                Log.d("ConversationViewModel", "Loading conversations for user: $userId")
                val conversationList = conversationRepository.getConversations(userId)
                Log.d("ConversationViewModel", "Conversations loaded: ${conversationList.size}")
                _conversations.postValue(conversationList)
            } catch (e: Exception) {
                Log.e("ConversationViewModel", "Error loading conversations: ${e.message}")
            }
        }
    }
}

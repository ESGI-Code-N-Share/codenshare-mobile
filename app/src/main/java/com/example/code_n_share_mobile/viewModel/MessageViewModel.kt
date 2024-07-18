package com.example.code_n_share_mobile.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_n_share_mobile.models.Message
import com.example.code_n_share_mobile.models.SendMessageRequest
import com.example.code_n_share_mobile.repositories.MessageRepository
import kotlinx.coroutines.launch

class MessageViewModel(private val messageRepository: MessageRepository) : ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    fun loadMessages(userId: String, userEmail: String, conversationId: String) {
        viewModelScope.launch {
            try {
                val messageList = messageRepository.getMessages(userId, conversationId)
                messageList.forEach { message ->
                    message.isSentByCurrentUser = message.sender?.email == userEmail
                }
                _messages.postValue(messageList)
            } catch (e: Exception) {
                Log.e("MessageViewModel", "Error loading messages: ${e.message}")
            }
        }
    }

    fun sendMessage(userId: String, userEmail: String, conversationId: String, content: String) {
        viewModelScope.launch {
            try {
                val request = SendMessageRequest(conversationId, userId, content)
                messageRepository.sendMessage(userId, conversationId, request)
                loadMessages(userId, userEmail, conversationId)
            } catch (e: Exception) {
                Log.e("MessageViewModel", "Error sending message: ${e.message}")
            }
        }
    }
}

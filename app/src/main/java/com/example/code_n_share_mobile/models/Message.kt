package com.example.code_n_share_mobile.models

data class Message(
    val content: String,
    val conversationId: String,
    val sender: User?,
    var isSentByCurrentUser: Boolean = false
)

data class SendMessageRequest(
    val conversationId: String,
    val senderId: String,
    val content: String
)

data class MessageResponse(
    val data: List<Message>
)
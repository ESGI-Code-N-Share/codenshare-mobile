import com.example.code_n_share_mobile.models.User

data class Conversation(
    val conversationId: String,
    val owner: User,
    val members: List<User>,
    val messages: List<Message>,
    val createdAt: String,
    val description: String,
)

data class Message(
    val messageId: String,
    val senderId: String,
    val content: String,
    val timestamp: String
)

data class ConversationResponse(
    val data: List<Conversation>
)


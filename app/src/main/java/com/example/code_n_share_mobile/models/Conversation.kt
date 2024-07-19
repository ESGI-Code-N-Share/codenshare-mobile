import com.example.code_n_share_mobile.models.User
import java.util.Date

data class Conversation(
    val conversationId: String,
    val owner: User,
    val members: List<User>,
    val messages: List<Message>,
    val createdAt: Date,
    val description: String? = null
)


data class Message(
    val messageId: String,
    val senderId: String,
    val content: String,
    val sendAt: Date
)

data class ConversationResponse(
    val data: List<Conversation>
)

data class CreateConversationRequest(
    val ownerId: String,
    val memberIds: List<String>
)


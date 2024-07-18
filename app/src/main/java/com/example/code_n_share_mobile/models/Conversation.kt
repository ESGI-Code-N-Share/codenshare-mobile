import com.example.code_n_share_mobile.models.Message
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

data class ConversationResponse(
    val data: List<Conversation>
)

data class CreateConversationRequest(
    val ownerId: String,
    val memberIds: List<String>
)


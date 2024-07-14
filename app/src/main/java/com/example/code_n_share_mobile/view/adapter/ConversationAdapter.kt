import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.code_n_share_mobile.R

class ConversationAdapter(private var conversations: List<Conversation>) :
    RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conversation, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversation = conversations[position]
        Log.d("ConversationAdapter", "Binding conversation at position $position: ${conversation.description}")
        holder.bind(conversation)
    }

    override fun getItemCount(): Int {
        return conversations.size
    }

    fun updateConversations(newConversations: List<Conversation>) {
        Log.d("ConversationAdapter", "Updating conversations: ${newConversations.size}")
        conversations = newConversations
        notifyDataSetChanged()
    }

    class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvConversationTitle: TextView = itemView.findViewById(R.id.tv_conversation_title)
        fun bind(conversation: Conversation) {
            tvConversationTitle.text = conversation.description ?: "No description"
        }
    }
}

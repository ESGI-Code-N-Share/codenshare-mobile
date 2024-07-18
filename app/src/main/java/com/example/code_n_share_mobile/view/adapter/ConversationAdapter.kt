package com.example.code_n_share_mobile.view.adapter

import Conversation
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.code_n_share_mobile.R
import com.example.code_n_share_mobile.view.MessageActivity

class ConversationAdapter(
    private var conversations: List<Conversation>,
    private val context: Context,
    private val onDeleteClick: (Conversation) -> Unit
) : RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conversation, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversation = conversations[position]
        Log.d("ConversationAdapter", "Binding conversation at position $position: ${conversation.owner.firstname}")
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

    inner class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvConversationTitle: TextView = itemView.findViewById(R.id.tv_conversation_title)
        private val tvLastMessage: TextView = itemView.findViewById(R.id.tv_last_message)
        private val btnDeleteConversation: ImageView = itemView.findViewById(R.id.btn_delete_conversation)

        fun bind(conversation: Conversation) {
            val memberNames = conversation.members.joinToString(", ") { it.firstname }
            tvConversationTitle.text = memberNames

            val lastMessage = conversation.messages.lastOrNull()?.content ?: "No messages"
            tvLastMessage.text = lastMessage

            itemView.setOnClickListener {
                val intent = Intent(context, MessageActivity::class.java)
                intent.putExtra("conversationId", conversation.conversationId)
                context.startActivity(intent)
            }

            btnDeleteConversation.setOnClickListener {
                onDeleteClick(conversation)
            }
        }
    }
}

package com.example.code_n_share_mobile.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.code_n_share_mobile.R
import com.example.code_n_share_mobile.view.adapter.ConversationAdapter
import com.example.code_n_share_mobile.viewModel.ConversationViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConversationActivity : BaseActivity() {

    private val conversationViewModel: ConversationViewModel by viewModel()
    private lateinit var conversationAdapter: ConversationAdapter
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var fabCreateConversation: FloatingActionButton
    private lateinit var noConversationsMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        setupBottomNavigation(R.id.nav_conversations)

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_conversations)
        loadingProgressBar = findViewById(R.id.loading_progress_bar)
        fabCreateConversation = findViewById(R.id.fab_create_conversation)
        noConversationsMessage = findViewById(R.id.no_conversations_message)

        recyclerView.layoutManager = LinearLayoutManager(this)
        conversationAdapter = ConversationAdapter(emptyList(), this) { conversation ->
            deleteConversation(conversation.conversationId)
        }
        recyclerView.adapter = conversationAdapter

        loadUserConversations()

        fabCreateConversation.setOnClickListener {
            startActivity(Intent(this, CreateConversationActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        loadUserConversations()
    }

    private fun loadUserConversations() {
        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null) ?: return
        Log.d("ConversationActivity", "Loading conversations for user: $userId")
        conversationViewModel.loadConversations(userId)

        conversationViewModel.conversations.observe(this) { conversations ->
            Log.d("ConversationActivity", "Conversations observed: ${conversations.size}")
            loadingProgressBar.visibility = View.GONE
            if (conversations.isEmpty()) {
                noConversationsMessage.visibility = View.VISIBLE
            } else {
                noConversationsMessage.visibility = View.GONE
            }
            conversationAdapter.updateConversations(conversations)
        }

        loadingProgressBar.visibility = View.VISIBLE
    }

    private fun deleteConversation(conversationId: String) {
        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null) ?: return
        conversationViewModel.deleteConversation(userId, conversationId)
    }
}


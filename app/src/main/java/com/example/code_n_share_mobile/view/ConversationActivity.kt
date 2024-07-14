package com.example.code_n_share_mobile.view

import ConversationAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.code_n_share_mobile.R
import com.example.code_n_share_mobile.viewModel.ConversationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConversationActivity : BaseActivity() {

    private val conversationViewModel: ConversationViewModel by viewModel()
    private lateinit var conversationAdapter: ConversationAdapter
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)
        setupBottomNavigation()

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view_conversations)
        loadingProgressBar = findViewById(R.id.loading_progress_bar)
        recyclerView.layoutManager = LinearLayoutManager(this)
        conversationAdapter = ConversationAdapter(emptyList())
        recyclerView.adapter = conversationAdapter

        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null) ?: return
        Log.d("ConversationActivity", "Loading conversations for user: $userId")
        conversationViewModel.loadConversations(userId)

        conversationViewModel.conversations.observe(this) { conversations ->
            Log.d("ConversationActivity", "Conversations observed: ${conversations.size}")
            loadingProgressBar.visibility = View.GONE
            conversationAdapter.updateConversations(conversations)
        }

        loadingProgressBar.visibility = View.VISIBLE
    }
}

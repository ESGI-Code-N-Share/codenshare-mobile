package com.example.code_n_share_mobile.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.code_n_share_mobile.R
import com.example.code_n_share_mobile.models.User
import com.example.code_n_share_mobile.view.adapter.UserAdapter
import com.example.code_n_share_mobile.viewModel.ConversationViewModel
import com.example.code_n_share_mobile.viewModel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateConversationActivity : AppCompatActivity() {

    private val conversationViewModel: ConversationViewModel by viewModel()
    private val userViewModel: UserViewModel by viewModel()

    private lateinit var searchUserAdapter: UserAdapter
    private lateinit var selectedUserAdapter: UserAdapter
    private val selectedUsers = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_conversation)

        val etSearchUser: EditText = findViewById(R.id.et_search_user)
        val rvSearchResults: RecyclerView = findViewById(R.id.rv_search_results)
        val rvSelectedUsers: RecyclerView = findViewById(R.id.rv_selected_users)
        val tvSelectedUsers: TextView = findViewById(R.id.tv_selected_users)
        val btnCreate: Button = findViewById(R.id.btn_create)

        setupAdapters(rvSearchResults, rvSelectedUsers, tvSelectedUsers)
        setupSearchBar(etSearchUser, rvSearchResults)

        btnCreate.setOnClickListener {
            val memberIds = selectedUsers.map { it.userId }
            createConversation(memberIds)
            startActivity(Intent(this, ConversationActivity::class.java))
            Toast.makeText(this, "Conversation created", Toast.LENGTH_SHORT).show()
        }

        conversationViewModel.creationResult.observe(this) {
            Toast.makeText(this, "Conversation created", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun setupAdapters(rvSearchResults: RecyclerView, rvSelectedUsers: RecyclerView, tvSelectedUsers: TextView) {
        searchUserAdapter = UserAdapter(emptyList()) { user ->
            selectedUsers.add(user)
            selectedUserAdapter.updateUsers(selectedUsers)
            rvSelectedUsers.visibility = View.VISIBLE
            tvSelectedUsers.visibility = View.VISIBLE
        }

        selectedUserAdapter = UserAdapter(emptyList()) { user ->
            selectedUsers.remove(user)
            selectedUserAdapter.updateUsers(selectedUsers)
            if (selectedUsers.isEmpty()) {
                rvSelectedUsers.visibility = View.GONE
                tvSelectedUsers.visibility = View.GONE
            }
        }

        rvSearchResults.layoutManager = LinearLayoutManager(this)
        rvSearchResults.adapter = searchUserAdapter

        rvSelectedUsers.layoutManager = LinearLayoutManager(this)
        rvSelectedUsers.adapter = selectedUserAdapter
    }

    private fun setupSearchBar(etSearchUser: EditText, rvSearchResults: RecyclerView) {
        etSearchUser.doBeforeTextChanged { text, _, _, _ ->
            if (text.isNullOrBlank()) {
                rvSearchResults.visibility = View.GONE
            }
        }

        etSearchUser.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrBlank()) {
                userViewModel.searchUsers(text.toString())
                rvSearchResults.visibility = View.VISIBLE
            } else {
                rvSearchResults.visibility = View.GONE
            }
        }

        userViewModel.searchResults.observe(this) { users ->
            searchUserAdapter.updateUsers(users)
        }
    }

    private fun createConversation(memberIds: List<String>) {
        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val ownerId = sharedPreferences.getString("userId", null) ?: return
        conversationViewModel.createConversation(ownerId, memberIds)
    }
}

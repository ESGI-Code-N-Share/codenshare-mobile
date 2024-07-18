package com.example.code_n_share_mobile.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.code_n_share_mobile.R
import com.example.code_n_share_mobile.models.User
import com.example.code_n_share_mobile.view.adapter.UserAdapter
import com.example.code_n_share_mobile.viewModel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchUserActivity : BaseActivity() {

    private val userViewModel: UserViewModel by viewModel()
    private lateinit var resultUsersRecyclerView: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var searchBarCancelButton: TextView
    private lateinit var listHeaderSection: TextView
    private lateinit var loadingProgressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_user)

        setupBottomNavigation(R.id.nav_search)

        this.resultUsersRecyclerView = findViewById(R.id.result_users_recycler_view)
        this.searchBar = findViewById(R.id.search_user_input)
        this.searchBarCancelButton = findViewById(R.id.search_user_input_cancel)
        this.listHeaderSection = findViewById(R.id.list_header_title)
        this.loadingProgressBar = findViewById(R.id.loading_search_page)

        this.setSearchBar()
        this.observeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        userViewModel.searchResults.removeObservers(this)
    }

    private fun setSearchBar() {
        this.searchBar.doBeforeTextChanged { text, _, _, _ ->
            if (text.isNullOrBlank()) {
                loadingProgressBar.visibility = View.GONE
            }
        }
        this.searchBar.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrBlank()) {
                loadingProgressBar.visibility = View.VISIBLE
                this.userViewModel.searchUsers(text.toString())
            }
        }
        this.searchBarCancelButton.setOnClickListener {
            this.searchBar.text.clear()
            this.setUpResultUsers(emptyList())
            this.searchBarCancelButton.visibility = View.GONE
        }
    }

    private fun setUpResultUsers(users: List<User>) {
        val resultUserAdapter = UserAdapter(users) { user ->
            navigateToProfile(user)
        }
        this.resultUsersRecyclerView.layoutManager = LinearLayoutManager(this)
        this.resultUsersRecyclerView.adapter = resultUserAdapter
        this.resultUsersRecyclerView.visibility = if (users.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun observeViewModel() {
        userViewModel.searchResults.observe(this) { users ->
            this.setUpResultUsers(users)
            loadingProgressBar.visibility = View.GONE
        }
    }

    private fun navigateToProfile(user: User) {
        Intent(this, ProfileActivity::class.java).also {
            it.putExtra("userId", user.userId)
            it.putExtra("firstname", user.firstname)
            it.putExtra("lastname", user.lastname)
            it.putExtra("email", user.email)
            it.putExtra("avatarUrl", user.avatar)
            startActivity(it)

        }
    }
}

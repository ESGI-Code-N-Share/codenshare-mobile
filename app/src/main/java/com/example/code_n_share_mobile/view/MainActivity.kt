package com.example.code_n_share_mobile.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.code_n_share_mobile.R
import com.example.code_n_share_mobile.di.injectModuleDependencies
import com.example.code_n_share_mobile.di.parseAndInjectConfiguration
import com.example.code_n_share_mobile.view.adapter.PostAdapter
import com.example.code_n_share_mobile.viewModel.AuthViewModel
import com.example.code_n_share_mobile.viewModel.PostViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity() {

    private val authViewModel: AuthViewModel by viewModel()
    private val postViewModel: PostViewModel by viewModel()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter
    private lateinit var fabCreatePost: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        parseAndInjectConfiguration()
        injectModuleDependencies(this)
        setupBottomNavigation(R.id.nav_home)

        this.recyclerView = findViewById(R.id.recycler_view)
        this.fabCreatePost = findViewById(R.id.fab_create_post)

        recyclerView.layoutManager = LinearLayoutManager(this)

        fabCreatePost.setOnClickListener {
            showCreatePostDialog()
        }

        observeViewModel()
        checkLoginStatus()
    }

    override fun onDestroy() {
        super.onDestroy()
        authViewModel.logoutResult.removeObservers(this)
        postViewModel.posts.removeObservers(this)
    }

    private fun observeViewModel() {
        authViewModel.logoutResult.observe(this) { result ->
            result?.let {
                if (it.success) {
                    checkLoginStatus()
                    clearPosts()
                } else {
                    showToast("Error logging out: ${it.error}")
                }
            }
        }

        postViewModel.posts.observe(this) { posts ->
            val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
            val userId = sharedPreferences.getString("userId", null) ?: ""
            adapter = PostAdapter(posts, userId,
                { postId, userId -> postViewModel.deletePost(postId, userId) },
                { postId -> postViewModel.likePost(postId, userId) },
                { postId -> postViewModel.unlikePost(postId, userId) }
            )
            recyclerView.adapter = adapter
            Log.d("MainActivity", "Posts updated: ${posts.size} posts received")
        }
    }

    private fun loadPosts() {
        postViewModel.loadPosts()
    }

    private fun clearPosts() {
        postViewModel.clearPosts()
        adapter = PostAdapter(emptyList(), "",
            { _, _ -> },
            { _ -> },
            { _ -> }
        )
        recyclerView.adapter = adapter
        Log.d("MainActivity", "Posts cleared")
    }

    private fun checkLoginStatus() {
        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        if (token != null) {
            fabCreatePost.visibility = View.VISIBLE
            loadPosts()
        } else {
//            clearSharedPreferences(sharedPreferences)
            startActivity(Intent(this, IntroductionActivity::class.java))
            finish()
        }
    }

    private fun showCreatePostDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_create_post, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.et_post_title)
        val etContent = dialogView.findViewById<EditText>(R.id.et_post_content)
        val etImage = dialogView.findViewById<EditText>(R.id.et_post_image)

        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.create_post)
            .setView(dialogView)
            .setPositiveButton(R.string.create) { _, _ ->
                val title = etTitle.text.toString()
                val content = etContent.text.toString()
                val image = etImage.text.toString()
                val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
                val authorId = sharedPreferences.getString("userId", null)
                if (authorId != null) {
                    postViewModel.createPost(authorId, title, content, image)
                } else {
                    Log.e("MainActivity", "User ID not found in SharedPreferences")
                }
            }
            .setNegativeButton(R.string.cancel, null)
            .create()

        dialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

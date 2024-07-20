package com.example.code_n_share_mobile.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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

    private var selectedImageUrl: String? = null
    private val defaultImageUrls = listOf(
        "https://randomwordgenerator.com/img/picture-generator/55e4d5464f5ba914f1dc8460962e33791c3ad6e04e5074417d2d73dc934bcd_640.jpg",
        "https://randomwordgenerator.com/img/picture-generator/55e2dc454c5aaa14f1dc8460962e33791c3ad6e04e507441722872d59f4ac3_640.jpg",
        "https://randomwordgenerator.com/img/picture-generator/54e6d5464f52ae14f1dc8460962e33791c3ad6e04e50744172297cdc9e48c3_640.jpg",
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        parseAndInjectConfiguration()
        injectModuleDependencies(this)
        setupBottomNavigation(R.id.nav_home)

        this.recyclerView = findViewById(R.id.recycler_view)
        this.fabCreatePost = findViewById(R.id.fab_create_post)

        setupRecyclerView()

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

    private fun setupRecyclerView() {
        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "") ?: ""
        adapter = PostAdapter(
            posts = emptyList(),
            userId = userId,
            onDeletePost = { postId, userId -> postViewModel.deletePost(postId, userId) },
            onLikePost = { postId -> postViewModel.likePost(postId, userId) },
            onUnlikePost = { postId -> postViewModel.unlikePost(postId, userId) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
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
            adapter.updatePosts(posts)
            Log.d("MainActivity", "Posts updated: ${posts.size} posts received")
        }
    }

    private fun loadPosts() {
        postViewModel.loadPosts()
    }

    private fun clearPosts() {
        postViewModel.clearPosts()
        adapter.updatePosts(emptyList())
        Log.d("MainActivity", "Posts cleared")
    }

    private fun checkLoginStatus() {
        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        if (token != null) {
            fabCreatePost.visibility = View.VISIBLE
            loadPosts()
        } else {
            startActivity(Intent(this, IntroductionActivity::class.java))
            finish()
        }
    }

    private fun showCreatePostDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_create_post, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.et_post_title)
        val etContent = dialogView.findViewById<EditText>(R.id.et_post_content)
        val radioGroupImages = dialogView.findViewById<RadioGroup>(R.id.radio_group_images)
        val imgPreview = dialogView.findViewById<ImageView>(R.id.img_preview)

        radioGroupImages.setOnCheckedChangeListener { group, checkedId ->
            selectedImageUrl = when (checkedId) {
                R.id.radio_image_1 -> defaultImageUrls[0]
                R.id.radio_image_2 -> defaultImageUrls[1]
                R.id.radio_image_3 -> defaultImageUrls[2]
                else -> null
            }
            selectedImageUrl?.let { url ->
                imgPreview.visibility = View.VISIBLE
                Glide.with(this).load(url).into(imgPreview)
            }
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle(R.string.create_post)
            .setView(dialogView)
            .setPositiveButton(R.string.create) { _, _ ->
                val title = etTitle.text.toString()
                val content = etContent.text.toString()
                val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
                val authorId = sharedPreferences.getString("userId", null)
                if (authorId != null) {
                    postViewModel.createPost(authorId, title, content, selectedImageUrl)
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

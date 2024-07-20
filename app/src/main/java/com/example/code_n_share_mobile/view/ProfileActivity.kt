package com.example.code_n_share_mobile.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.code_n_share_mobile.R
import com.example.code_n_share_mobile.models.EditUser
import com.example.code_n_share_mobile.models.User
import com.example.code_n_share_mobile.view.adapter.PostAdapter
import com.example.code_n_share_mobile.viewModel.PostViewModel
import com.example.code_n_share_mobile.viewModel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : BaseActivity() {

    private val userViewModel: UserViewModel by viewModel()
    private val postViewModel: PostViewModel by viewModel()

    private lateinit var imgProfile: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvHandle: TextView
    private lateinit var tvFollowers: TextView
    private lateinit var tvFollowing: TextView
    private lateinit var tvOverview: TextView
    private lateinit var btnFollow: Button
    private lateinit var btnEditProfile: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter

    private lateinit var currentUser: User
    private lateinit var loggedInUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupBottomNavigation(R.id.nav_profile)

        imgProfile = findViewById(R.id.img_profile)
        tvName = findViewById(R.id.tv_name)
        tvHandle = findViewById(R.id.tv_username)
        tvFollowers = findViewById(R.id.tv_followers)
        tvFollowing = findViewById(R.id.tv_following)
        tvOverview = findViewById(R.id.tv_overview)
        btnFollow = findViewById(R.id.btn_follow)
        btnEditProfile = findViewById(R.id.btn_edit_profile)
        recyclerView = findViewById(R.id.recycler_view)

        loggedInUserId = getSharedPreferences("auth", Context.MODE_PRIVATE).getString("userId", "") ?: ""

        val userId = intent.getStringExtra("userId") ?: ""
        val firstname = intent.getStringExtra("firstname") ?: ""
        val lastname = intent.getStringExtra("lastname") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val avatarUrl = intent.getStringExtra("avatarUrl") ?: ""
        val overview = intent.getStringExtra("overview") ?: ""

        val userName = "$firstname $lastname"
        val userUsername = "@${email.split("@")[0]}"

        tvName.text = userName
        tvHandle.text = userUsername
        tvOverview.text = overview

        Glide.with(this)
            .load(avatarUrl)
            .circleCrop()
            .into(imgProfile)

        currentUser = User(userId, firstname, lastname, email, "", avatarUrl, overview)

        if (userId == loggedInUserId) {
            btnFollow.visibility = View.GONE
            btnEditProfile.visibility = View.VISIBLE
        } else {
            btnFollow.visibility = View.VISIBLE
            btnEditProfile.visibility = View.GONE
            checkIfFollowing()

            btnFollow.setOnClickListener {
                if (btnFollow.text == "Follow") {
                    userViewModel.followUser(loggedInUserId, currentUser.userId)
                } else {
                    userViewModel.unfollowUser(loggedInUserId, currentUser.userId)
                }
            }
        }

        btnEditProfile.setOnClickListener {
            showEditProfileDialog()
        }

        setupRecyclerView()
        observeViewModel()
        userViewModel.getFollowers(userId)
        userViewModel.getFollowing(userId)
        postViewModel.loadPostsForUser(userId)
    }

    override fun onDestroy() {
        super.onDestroy()
        userViewModel.followers.removeObservers(this)
        userViewModel.following.removeObservers(this)
        userViewModel.followResult.removeObservers(this)
        userViewModel.unfollowResult.removeObservers(this)
    }

    private fun setupRecyclerView() {
        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "") ?: ""
        postAdapter = PostAdapter(
            posts = emptyList(),
            userId = userId,
            onDeletePost = { postId, userId -> postViewModel.deletePost(postId, userId) },
            onLikePost = { postId -> postViewModel.likePost(postId, userId) },
            onUnlikePost = { postId -> postViewModel.unlikePost(postId, userId) }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = postAdapter
    }

    private fun deletePost(postId: String, userId: String) {
        postViewModel.deletePost(postId, userId)
    }

    private fun likePost(postId: String) {
        postViewModel.likePost(postId, loggedInUserId)
    }

    private fun unlikePost(postId: String) {
        postViewModel.unlikePost(postId, loggedInUserId)
    }

    private fun checkIfFollowing() {
        Log.d("ProfileActivity", "Checking if logged in user with ID: $loggedInUserId is following user with ID: ${currentUser.userId}")

        userViewModel.getFollowing(loggedInUserId)
        userViewModel.following.observe(this) { following ->
            Log.d("ProfileActivity", "Following list retrieved: ${following.size} users")
            following.forEach { user ->
                Log.d("ProfileActivity", "Following user: ${user.userId} - ${user.firstname} ${user.lastname}")
            }

            val isFollowing = following.any { it.userId == currentUser.userId }
            Log.d("ProfileActivity", "Is logged in user following current user: $isFollowing")

            btnFollow.text = if (isFollowing) "Unfollow" else "Follow"
        }
    }

    private fun observeViewModel() {
        userViewModel.followResult.observe(this) { result ->
            result?.let {
                if (it.success) {
                    btnFollow.text = "Unfollow"
                    userViewModel.getFollowers(currentUser.userId)
                } else {
                    showToast("Error following user: ${it.error}")
                }
            }
        }

        userViewModel.unfollowResult.observe(this) { result ->
            result?.let {
                if (it.success) {
                    btnFollow.text = "Follow"
                    userViewModel.getFollowers(currentUser.userId)
                } else {
                    showToast("Error unfollowing user: ${it.error}")
                }
            }
        }

        userViewModel.followers.observe(this) { followers ->
            tvFollowers.text = "Followers: ${followers.size}"
        }

        userViewModel.following.observe(this) { following ->
            tvFollowing.text = "Following: ${following.size}"
        }

        postViewModel.posts.observe(this) { posts ->
            postAdapter.updatePosts(posts)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_profile, null)
        val etFirstname = dialogView.findViewById<EditText>(R.id.et_firstname)
        val etLastname = dialogView.findViewById<EditText>(R.id.et_lastname)
        val etEmail = dialogView.findViewById<EditText>(R.id.et_email)
        val etOverview = dialogView.findViewById<EditText>(R.id.et_overview)

        etFirstname.setText(currentUser.firstname)
        etLastname.setText(currentUser.lastname)
        etEmail.setText(currentUser.email)
        etOverview.setText(currentUser.overview)

        AlertDialog.Builder(this)
            .setTitle("Edit Profile")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val updatedUser = EditUser(
                    firstname = etFirstname.text.toString(),
                    lastname = etLastname.text.toString(),
                    email = etEmail.text.toString(),
                    overview = etOverview.text.toString()
                )
                updateUserProfile(loggedInUserId, updatedUser)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateUserProfile(userId: String, updatedUser: EditUser) {
        userViewModel.updateUserProfile(userId, updatedUser)

        tvName.text = "${updatedUser.firstname} ${updatedUser.lastname}"
        tvHandle.text = "@${updatedUser.email.split("@")[0]}"
        tvOverview.text = updatedUser.overview
        currentUser = currentUser.copy(
            firstname = updatedUser.firstname,
            lastname = updatedUser.lastname,
            email = updatedUser.email,
            overview = updatedUser.overview
        )
    }
}

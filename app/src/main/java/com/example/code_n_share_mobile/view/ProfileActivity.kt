package com.example.code_n_share_mobile.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.code_n_share_mobile.R
import com.example.code_n_share_mobile.models.User
import com.example.code_n_share_mobile.viewModel.UserViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : BaseActivity() {

    private val userViewModel: UserViewModel by viewModel()

    private lateinit var imgProfile: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvHandle: TextView
    private lateinit var tvFollowers: TextView
    private lateinit var tvFollowing: TextView
    private lateinit var btnFollow: Button

    private lateinit var currentUser: User
    private lateinit var loggedInUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        setupBottomNavigation()

        imgProfile = findViewById(R.id.img_profile)
        tvName = findViewById(R.id.tv_name)
        tvHandle = findViewById(R.id.tv_username)
        tvFollowers = findViewById(R.id.tv_followers)
        tvFollowing = findViewById(R.id.tv_following)
        btnFollow = findViewById(R.id.btn_follow)

        loggedInUserId = getSharedPreferences("auth", Context.MODE_PRIVATE).getString("userId", "") ?: ""

        val userId = intent.getStringExtra("userId") ?: ""
        val firstname = intent.getStringExtra("firstname") ?: ""
        val lastname = intent.getStringExtra("lastname") ?: ""
        val email = intent.getStringExtra("email") ?: ""
        val avatarUrl = intent.getStringExtra("avatarUrl") ?: ""

        val userName = "$firstname $lastname"
        val userUsername = "@${email.split("@")[0]}"

        tvName.text = userName
        tvHandle.text = userUsername

        Glide.with(this)
            .load(avatarUrl)
            .circleCrop()
            .into(imgProfile)

        currentUser = User(userId, firstname, lastname, email, "", avatarUrl)

        if (userId == loggedInUserId) {
            btnFollow.visibility = View.GONE
        } else {
            checkIfFollowing()

            btnFollow.setOnClickListener {
                if (btnFollow.text == "Follow") {
                    userViewModel.followUser(loggedInUserId, currentUser.userId)
                } else {
                    userViewModel.unfollowUser(loggedInUserId, currentUser.userId)
                }
            }
        }

        observeViewModel()
        userViewModel.getFollowers(userId)
        userViewModel.getFollowing(userId)
    }

    override fun onDestroy() {
        super.onDestroy()
        userViewModel.followers.removeObservers(this)
        userViewModel.following.removeObservers(this)
        userViewModel.followResult.removeObservers(this)
        userViewModel.unfollowResult.removeObservers(this)
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
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

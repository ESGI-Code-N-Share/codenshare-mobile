package com.example.code_n_share_mobile.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.code_n_share_mobile.R
import com.example.code_n_share_mobile.viewModel.AuthViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

abstract class BaseActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun setupBottomNavigation(selectedItemId: Int) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.selectedItemId = selectedItemId
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                if (!isCurrentActivity(MainActivity::class.java)) {
                    startActivity(Intent(this, MainActivity::class.java))
                }
                return true
            }
            R.id.nav_search -> {
                if (!isCurrentActivity(SearchUserActivity::class.java)) {
                    startActivity(Intent(this, SearchUserActivity::class.java))
                }
                return true
            }
            R.id.nav_profile -> {
                if (!isCurrentActivity(ProfileActivity::class.java)) {
                    navigateToProfile()
                }
                return true
            }
            R.id.nav_conversations -> {
                if (!isCurrentActivity(ConversationActivity::class.java)) {
                    startActivity(Intent(this, ConversationActivity::class.java))
                }
                return true
            }
            R.id.nav_logout -> {
                authViewModel.logoutUser(this)
                return true
            }
        }
        return false
    }

    private fun isCurrentActivity(activityClass: Class<*>): Boolean {
        return this::class.java == activityClass
    }

    private fun navigateToProfile() {
        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "") ?: ""
        val firstname = sharedPreferences.getString("firstname", "") ?: ""
        val lastname = sharedPreferences.getString("lastname", "") ?: ""
        val email = sharedPreferences.getString("email", "") ?: ""
        val avatarUrl = sharedPreferences.getString("avatarUrl", "") ?: ""
        val overview = sharedPreferences.getString("overview", "") ?: ""

        Intent(this, ProfileActivity::class.java).also {
            it.putExtra("userId", userId)
            it.putExtra("firstname", firstname)
            it.putExtra("lastname", lastname)
            it.putExtra("email", email)
            it.putExtra("avatarUrl", avatarUrl)
            it.putExtra("overview", overview)
            startActivity(it)
        }
    }
}

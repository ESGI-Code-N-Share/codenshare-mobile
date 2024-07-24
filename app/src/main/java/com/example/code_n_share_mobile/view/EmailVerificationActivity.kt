package com.example.code_n_share_mobile.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.code_n_share_mobile.R

class EmailVerificationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verification)

        val tvVerificationMessage = findViewById<TextView>(R.id.tv_verification_message)
        val btnGoToLogin = findViewById<Button>(R.id.btn_go_to_login)

        val isSuccess = intent?.getBooleanExtra("success", false) ?: false
        if (isSuccess) {
            tvVerificationMessage.text = "Verify your email before login"
            btnGoToLogin.setOnClickListener {
                navigateToLogin()
            }
        } else {
            Toast.makeText(this, "Email verification failed", Toast.LENGTH_SHORT).show()
            tvVerificationMessage.text = "Email verification failed"
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}

package com.example.code_n_share_mobile.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.code_n_share_mobile.R

class IntroductionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_activity_introduction)

        val btnGoToLogin: Button = findViewById(R.id.btn_go_to_login)
        val btnGoToRegister: Button = findViewById(R.id.btn_go_to_register)

        btnGoToLogin.setOnClickListener {
            Intent(this, LoginActivity::class.java).also { startActivity(it) }
        }

        btnGoToRegister.setOnClickListener {
            Intent(this, RegisterActivity::class.java).also { startActivity(it) }
        }
    }
}

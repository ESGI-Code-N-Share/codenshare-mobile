package com.example.code_n_share_mobile.view

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.code_n_share_mobile.R
import com.example.code_n_share_mobile.viewModel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModel()

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var cbStayLogin: CheckBox
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        cbStayLogin = findViewById(R.id.cb_stay_login)
        btnLogin = findViewById(R.id.btn_login)

        etEmail.setText("slimane.abdallah75@gmail.com")
        etPassword.setText("password")
        cbStayLogin.isChecked = true

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val stayLogin = cbStayLogin.isChecked

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Log.d("LoginActivity", "Attempting login with email: $email")
                authViewModel.loginUser(this, email, password, stayLogin)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        authViewModel.loginResult.observe(this) { result ->
            result.let {
                if (it.success) {
                    Log.d("LoginActivity", "Login page!")
                    Toast.makeText(this, "Login page!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("LoginActivity", "Login failed: ${it.error}")
//                    Toast.makeText(this, "Login failed: ${it.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

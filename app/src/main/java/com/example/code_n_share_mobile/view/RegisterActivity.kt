package com.example.code_n_share_mobile.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.code_n_share_mobile.R
import com.example.code_n_share_mobile.viewModel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etFirstname = findViewById<EditText>(R.id.et_firstname)
        val etLastname = findViewById<EditText>(R.id.et_lastname)
        val etEmail = findViewById<EditText>(R.id.et_email)
        val etPassword = findViewById<EditText>(R.id.et_password)
        val etBirthdate = findViewById<EditText>(R.id.et_birthdate)
        val btnRegister = findViewById<Button>(R.id.btn_register)

        btnRegister.setOnClickListener {
            val firstname = etFirstname.text.toString()
            val lastname = etLastname.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val birthdate = etBirthdate.text.toString()

            if (firstname.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && birthdate.isNotEmpty()) {
                authViewModel.registerUser(firstname, lastname, email, password, birthdate)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Observer for registration response
        authViewModel.registrationResult.observe(this) { result ->
            result?.let {
                if (it.success) {
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    // Navigate to another activity or perform further actions
                } else {
                    Toast.makeText(this, "Registration failed: ${it.error}", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}

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

    private lateinit var etFirstname: EditText
    private lateinit var etLastname: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etBirthdate: EditText
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        this.etFirstname = findViewById(R.id.et_firstname)
        this.etLastname = findViewById(R.id.et_lastname)
        this.etEmail = findViewById(R.id.et_email)
        this.etPassword = findViewById(R.id.et_password)
        this.etBirthdate = findViewById(R.id.et_birthdate)
        this.btnRegister = findViewById(R.id.btn_register)

        etFirstname.setText("John")
        etLastname.setText("Doe")
        etEmail.setText("slimane.abdallah75@gmail.com")
        etPassword.setText("password")
        etBirthdate.setText("2000-01-01")

        this.setupListeners()
        this.observeViewModel()
    }

    private fun setupListeners() {
        btnRegister.setOnClickListener {
            val firstname = etFirstname.text.toString()
            val lastname = etLastname.text.toString()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val birthdate = etBirthdate.text.toString()

            if (firstname.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && birthdate.isNotEmpty()) {
                authViewModel.registerUser(this, firstname, lastname, email, password, birthdate)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        authViewModel.registrationResult.observe(this) { result ->
            result?.let {
                if (it.success) {
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Registration failed: ${it.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        authViewModel.registrationResult.removeObservers(this)
    }
}

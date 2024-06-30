package com.example.code_n_share_mobile.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.code_n_share_mobile.R
import com.example.code_n_share_mobile.di.injectModuleDependencies
import com.example.code_n_share_mobile.di.parseAndInjectConfiguration
import com.example.code_n_share_mobile.viewModel.AuthViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val authViewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        parseAndInjectConfiguration()
        injectModuleDependencies(this)

        authViewModel.registerUser("John", "Doe", "john4.doe@example.com", "password", "2000-01-01")
    }
}

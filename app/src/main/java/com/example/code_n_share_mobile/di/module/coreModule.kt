package com.example.code_n_share_mobile.di.module

import com.example.code_n_share_mobile.repositories.UserRepository
import com.example.code_n_share_mobile.viewModel.AuthViewModel
import org.koin.dsl.module

internal val coreModule = module {
    single { UserRepository(get()) }

    single { AuthViewModel(get()) }
}
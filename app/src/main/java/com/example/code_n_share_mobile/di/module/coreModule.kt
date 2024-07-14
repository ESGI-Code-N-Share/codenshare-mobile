package com.example.code_n_share_mobile.di.module

import com.example.code_n_share_mobile.repositories.AuthRepository
import com.example.code_n_share_mobile.repositories.ConversationRepository
import com.example.code_n_share_mobile.repositories.PostRepository
import com.example.code_n_share_mobile.repositories.UserRepository
import com.example.code_n_share_mobile.viewModel.AuthViewModel
import com.example.code_n_share_mobile.viewModel.ConversationViewModel
import com.example.code_n_share_mobile.viewModel.PostViewModel
import com.example.code_n_share_mobile.viewModel.UserViewModel
import org.koin.dsl.module

internal val coreModule = module {
    single { UserRepository(get()) }
    single { AuthRepository(get()) }
    single { PostRepository(get()) }
    single { ConversationRepository(get()) }
    single { AuthViewModel(get()) }
    single { PostViewModel(get()) }
    single { UserViewModel(get()) }
    single { ConversationViewModel(get()) }
}

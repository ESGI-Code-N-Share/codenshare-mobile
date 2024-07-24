package com.example.code_n_share_mobile.di.module

import com.example.code_n_share_mobile.repositories.AuthRepository
import com.example.code_n_share_mobile.repositories.ConversationRepository
import com.example.code_n_share_mobile.repositories.MessageRepository
import com.example.code_n_share_mobile.repositories.PostRepository
import com.example.code_n_share_mobile.repositories.UserRepository
import com.example.code_n_share_mobile.viewModel.AuthViewModel
import com.example.code_n_share_mobile.viewModel.ConversationViewModel
import com.example.code_n_share_mobile.viewModel.MessageViewModel
import com.example.code_n_share_mobile.viewModel.PostViewModel
import com.example.code_n_share_mobile.viewModel.UserViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val coreModule = module {
    single { UserRepository(get()) }
    single { AuthRepository(get()) }
    single { PostRepository(get(), androidContext()) }
    single { MessageRepository(get()) }
    single { ConversationRepository(get()) }
    viewModel { AuthViewModel(get()) }
    viewModel { PostViewModel(get()) }
    viewModel { UserViewModel(get()) }
    viewModel { ConversationViewModel(get()) }
    viewModel { MessageViewModel(get()) }
}

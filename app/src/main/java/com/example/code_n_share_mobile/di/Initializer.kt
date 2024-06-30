package com.example.code_n_share_mobile.di

import android.content.Context
import com.example.code_n_share_mobile.BuildConfig
import com.example.code_n_share_mobile.di.module.coreModule
import com.example.code_n_share_mobile.di.module.remoteModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.error.KoinAppAlreadyStartedException
import org.koin.dsl.module

fun injectModuleDependencies(context: Context) {
    try {
        startKoin {
            androidContext(context)
            modules(modules)
        }
    } catch (alreadyStart: KoinAppAlreadyStartedException) {
        loadKoinModules(modules)
    }
}

fun parseAndInjectConfiguration() {
    val apiConf = ApiConfig(
        apiUrl = BuildConfig.CODENSHARE_API_URL,
        apiKey = BuildConfig.CODENSHARE_API_KEY
    )
    modules.add(
        module {
            single { apiConf }
        }
    )
}

private val modules = mutableListOf(coreModule, remoteModule)

data class ApiConfig(val apiUrl: String, val apiKey: String)

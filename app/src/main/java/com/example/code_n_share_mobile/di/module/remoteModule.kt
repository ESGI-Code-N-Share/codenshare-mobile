package com.example.code_n_share_mobile.di.module

import com.example.code_n_share_mobile.BuildConfig
import com.example.code_n_share_mobile.network.AuthApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal val remoteModule = module {
    single(named("authApiRetrofitClient")) {
        createRetrofitClient(get(), BuildConfig.CODENSHARE_API_URL, BuildConfig.CODENSHARE_API_KEY)
    }

    single { createOkHttpClient() }

    single {
        createWebService<AuthApiService>(
            get(named("authApiRetrofitClient"))
        )
    }
}

fun createOkHttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    return OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build()
}

fun createRetrofitClient(okhttpClient: OkHttpClient, apiUrl: String, apiKey: String): Retrofit {
    val gsonConverter = GsonConverterFactory.create(
        GsonBuilder().create()
    )

    return Retrofit.Builder()
        .baseUrl(apiUrl)
        .client(okhttpClient.newBuilder()
            .addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", apiKey)
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
            .build())
        .addConverterFactory(gsonConverter)
        .build()
}

inline fun <reified T> createWebService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}

package com.example.code_n_share_mobile.di.module

import android.content.Context
import com.example.code_n_share_mobile.BuildConfig
import com.example.code_n_share_mobile.network.AuthApiService
import com.example.code_n_share_mobile.network.ConversationApiService
import com.example.code_n_share_mobile.network.PostApiService
import com.example.code_n_share_mobile.network.UserApiService
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

internal val remoteModule = module {
    single { createOkHttpClient(get()) }
    single(named("authApiRetrofitClient")) { createRetrofitClient(get(), BuildConfig.CODENSHARE_API_URL) }
    single { createWebService<AuthApiService>(get(named("authApiRetrofitClient"))) }
    single { createWebService<PostApiService>(get(named("authApiRetrofitClient"))) }
    single { createWebService<UserApiService>(get(named("authApiRetrofitClient"))) }
    single { createWebService<ConversationApiService>(get(named("authApiRetrofitClient"))) }
}

fun createRetrofitClient(okhttpClient: OkHttpClient, apiUrl: String): Retrofit {
    val gsonConverter = GsonConverterFactory.create(GsonBuilder().create())
    return Retrofit.Builder()
        .baseUrl(apiUrl.replace("localhost", "10.0.2.2"))
        .client(okhttpClient)
        .addConverterFactory(gsonConverter)
        .build()
}

fun createOkHttpClient(context: Context): OkHttpClient {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    return OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()

            val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null)
            if (token != null && original.url.encodedPath.contains("register").not() &&
                original.url.encodedPath.contains("login").not() &&
                original.url.encodedPath.contains("verify-email").not()) {
                requestBuilder.header("Authorization", "Bearer $token")
            }

            val request = requestBuilder.method(original.method, original.body).build()
            chain.proceed(request)
        }
        .followRedirects(true)
        .followSslRedirects(true)
        .build()
}

inline fun <reified T> createWebService(retrofit: Retrofit): T {
    return retrofit.create(T::class.java)
}

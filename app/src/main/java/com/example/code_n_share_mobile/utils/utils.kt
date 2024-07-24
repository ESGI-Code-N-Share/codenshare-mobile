// Utils.kt
package com.example.code_n_share_mobile.utils

import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.HttpException

fun extractErrorMessage(e: Throwable): String {
    return if (e is HttpException) {
        val errorBody = e.response()?.errorBody()?.string()
        if (!errorBody.isNullOrEmpty()) {
            try {
                val json = Gson().fromJson(errorBody, JsonObject::class.java)
                json.getAsJsonObject("data")?.get("message")?.asString ?: "An error occurred"
            } catch (jsonException: Exception) {
                "An error occurred"
            }
        } else {
            "An error occurred"
        }
    } else {
        e.message ?: "An error occurred"
    }
}

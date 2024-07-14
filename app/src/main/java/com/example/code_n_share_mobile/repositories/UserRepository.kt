package com.example.code_n_share_mobile.repositories

import android.util.Log
import com.example.code_n_share_mobile.models.User
import com.example.code_n_share_mobile.network.UserApiService

class UserRepository(private val userApiService: UserApiService) {

    suspend fun searchUsers(query: String): List<User> {
        return try {
            val response = userApiService.searchUsers(query)
            response.data
        } catch (e: Exception) {
            Log.e("UserRepository", "Error searching users: ${e.message}")
            emptyList()
        }
    }

    suspend fun followUser(followerId: String, followedId: String) {
        try {
            val requestBody = mapOf("followerId" to followerId, "followedId" to followedId)
            userApiService.followUser(requestBody)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error following user: ${e.message}")
        }
    }

    suspend fun unfollowUser(followerId: String, followedId: String) {
        try {
            val requestBody = mapOf("followerId" to followerId, "followedId" to followedId)
            userApiService.unfollowUser(requestBody)
        } catch (e: Exception) {
            Log.e("UserRepository", "Error unfollowing user: ${e.message}")
        }
    }

    suspend fun getFollowing(userId: String): List<User> {
        Log.d("UserRepository", "Getting following users for userId: $userId")
        return try {
            val response = userApiService.getFollowingByUser(userId)
            response.data.filter { true }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error getting following users: ${e.message}")
            emptyList()
        }
    }

    suspend fun getFollowers(userId: String): List<User> {
        Log.d("UserRepository", "Fetching followers for userId: $userId")
        return try {
            val response = userApiService.getFollowers(userId)
            response.data.filter { true }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error fetching followers: ${e.message}")
            emptyList()
        }
    }

//    suspend fun verifyEmail(userId: String): VerifyEmailResponse {
//        return authApiService.verifyEmail(userId)
//    }
}

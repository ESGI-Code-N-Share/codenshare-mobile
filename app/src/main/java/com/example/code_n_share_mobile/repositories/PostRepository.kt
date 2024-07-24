package com.example.code_n_share_mobile.repositories

import android.content.Context
import android.util.Log
import com.example.code_n_share_mobile.models.CreatePost
import com.example.code_n_share_mobile.models.Post
import com.example.code_n_share_mobile.models.User
import com.example.code_n_share_mobile.network.PostApiService

class PostRepository(private val postApiService: PostApiService,private val context: Context) {

    suspend fun getPosts(): List<Post> {
        val response = postApiService.getPosts()
        val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", "") ?: ""

        return response.data.map { post ->
            post.copy(
                likesCount = post.likes.size,
                isLikedByUser = post.likes.any { it.userId == userId }
            )
        }
    }

    suspend fun getPostsForUser(userId: String): List<Post> {
        return try {
            val response = postApiService.getPosts()
            response.data.filter { it.author.userId == userId }.map { post ->
                val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                val loggedInUserId = sharedPreferences.getString("userId", "") ?: ""
                post.copy(
                    likesCount = post.likes.size,
                    isLikedByUser = post.likes.any { it.userId == loggedInUserId }
                )
            }
        } catch (e: Exception) {
            Log.e("PostRepository", "Error fetching posts for user: ${e.message}")
            emptyList()
        }
    }

    suspend fun createPost(authorId: String, title: String, content: String, image: String?) {
        Log.d("PostRepository", "Creating post for userId: $authorId")
        try {
            val createPost = CreatePost(authorId = authorId, title = title, content = content, image = image)
            postApiService.createPost(createPost)
            Log.d("PostRepository", "Post created successfully")
        } catch (e: Exception) {
            Log.e("PostRepository", "Error creating post: ${e.message}")
        }
    }

    suspend fun deletePost(postId: String, userId: String) {
        Log.d("PostRepository", "Deleting post with postId: $postId")
        try {
            val requestBody = mapOf("userId" to userId)
            postApiService.deletePost(postId, requestBody)
            Log.d("PostRepository", "Post deleted successfully")
        } catch (e: Exception) {
            Log.e("PostRepository", "Error deleting post: ${e.message}")
        }
    }

    suspend fun likePost(postId: String, userId: String) {
        Log.d("PostRepository", "Liking post with postId: $postId, userId: $userId")
        try {
            val requestBody = mapOf("userId" to userId)
            postApiService.likePost(postId, requestBody)
            Log.d("PostRepository", "Post liked successfully")
        } catch (e: Exception) {
            Log.e("PostRepository", "Error liking post: ${e.message}")
        }
    }

    suspend fun unlikePost(postId: String, userId: String) {
        Log.d("PostRepository", "Unliking post with postId: $postId, userId: $userId")
        try {
            val requestBody = mapOf("userId" to userId)
            postApiService.unlikePost(postId, requestBody)
            Log.d("PostRepository", "Post unliked successfully")
        } catch (e: Exception) {
            Log.e("PostRepository", "Error unliking post: ${e.message}")
        }
    }

    suspend fun getUserById(userId: String): User {
        Log.d("PostRepository", "Fetching user details for userId: $userId")
        return postApiService.getUserById(userId)
    }
}

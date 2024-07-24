package com.example.code_n_share_mobile.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_n_share_mobile.models.Post
import com.example.code_n_share_mobile.repositories.PostRepository
import com.example.code_n_share_mobile.utils.extractErrorMessage
import kotlinx.coroutines.launch

class PostViewModel(private val postRepository: PostRepository) : ViewModel() {

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> get() = _posts

    fun loadPosts() {
        viewModelScope.launch {
            try {
                val postList = postRepository.getPosts()
                _posts.postValue(postList)
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                Log.e("PostViewModel", "Error fetching posts: $errorMessage")
                _posts.postValue(emptyList())
            }
        }
    }

    fun loadPostsForUser(userId: String) {
        viewModelScope.launch {
            try {
                val postList = postRepository.getPostsForUser(userId)
                _posts.postValue(postList)
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                Log.e("PostViewModel", "Error fetching posts for user: $errorMessage")
                _posts.postValue(emptyList())
            }
        }
    }

    fun createPost(authorId: String, title: String, content: String, image: String?) {
        viewModelScope.launch {
            try {
                postRepository.createPost(authorId, title, content, image)
                loadPosts()
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                Log.e("PostViewModel", "Error creating post: $errorMessage")
            }
        }
    }

    fun deletePost(postId: String, userId: String) {
        viewModelScope.launch {
            try {
                Log.d("PostViewModel", "Deleting post with postId: $postId, userId: $userId")
                postRepository.deletePost(postId, userId)
                loadPosts()
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                Log.e("PostViewModel", "Error deleting post: $errorMessage")
            }
        }
    }

    fun likePost(postId: String, userId: String) {
        viewModelScope.launch {
            try {
                Log.d("PostViewModel", "Liking post with postId: $postId, userId: $userId")
                postRepository.likePost(postId, userId)
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                Log.e("PostViewModel", "Error liking post: $errorMessage")
            }
        }
    }

    fun unlikePost(postId: String, userId: String) {
        viewModelScope.launch {
            try {
                Log.d("PostViewModel", "Unliking post with postId: $postId, userId: $userId")
                postRepository.unlikePost(postId, userId)
            } catch (e: Exception) {
                val errorMessage = extractErrorMessage(e)
                Log.e("PostViewModel", "Error unliking post: $errorMessage")
            }
        }
    }

    fun clearPosts() {
        _posts.postValue(emptyList())
    }
}

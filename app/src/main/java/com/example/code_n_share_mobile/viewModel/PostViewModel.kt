package com.example.code_n_share_mobile.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.code_n_share_mobile.models.Post
import com.example.code_n_share_mobile.repositories.PostRepository
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
                Log.e("PostViewModel", "Error fetching posts: ${e.message}")
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
                Log.e("PostViewModel", "Error creating post: ${e.message}")
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
                Log.e("PostViewModel", "Error deleting post: ${e.message}")
            }
        }
    }

    fun likePost(postId: String, userId: String) {
        viewModelScope.launch {
            try {
                Log.d("PostViewModel", "Liking post with postId: $postId, userId: $userId")
                postRepository.likePost(postId, userId)
                updatePostLikeStatus(postId, true)
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error liking post: ${e.message}")
            }
        }
    }

    fun unlikePost(postId: String, userId: String) {
        viewModelScope.launch {
            try {
                Log.d("PostViewModel", "Unliking post with postId: $postId, userId: $userId")
                postRepository.unlikePost(postId, userId)
                updatePostLikeStatus(postId, false)
            } catch (e: Exception) {
                Log.e("PostViewModel", "Error unliking post: ${e.message}")
            }
        }
    }

    private fun updatePostLikeStatus(postId: String, isLiked: Boolean) {
        _posts.value = _posts.value?.map { post ->
            if (post.postId == postId) {
                Log.d("PostViewModel", "Updating like status for postId: $postId, isLiked: $isLiked")
                post.copy(isLikedByUser = isLiked, likesCount = if (isLiked) post.likesCount + 1 else post.likesCount - 1)
            } else {
                post
            }
        }
    }

    fun clearPosts() {
        _posts.postValue(emptyList())
    }
}

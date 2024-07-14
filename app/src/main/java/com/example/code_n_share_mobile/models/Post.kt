package com.example.code_n_share_mobile.models

data class Post(
    val postId: String,
    val title: String,
    val content: String,
    val author: User,
    val image: String?,
    val likesCount: Int,
    val isLikedByUser: Boolean
)

data class PostResponse(
    val data: List<Post>
)

data class CreatePost(
    val title: String,
    val content: String,
    val authorId: String,
    val image: String?
)




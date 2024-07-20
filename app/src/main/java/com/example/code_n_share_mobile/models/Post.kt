package com.example.code_n_share_mobile.models

data class Post(
    val postId: String,
    val title: String,
    val content: String,
    val author: User,
    val image: String?,
    var likesCount: Int,
    var isLikedByUser: Boolean,
    val likes: List<PostLike>
)

data class PostResponse(
    val data: List<Post>
)

data class PostLike(
    val postLikeId: String,
    val postId: String,
    val userId: String,
    val likedAt: String
)

data class CreatePost(
    val title: String,
    val content: String,
    val authorId: String,
    val image: String?
)




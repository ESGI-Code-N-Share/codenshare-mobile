package com.example.code_n_share_mobile.models

data class Friend(
    val followerId: String,
    val followedId: String
)

data class FriendResponse(
    val data: List<Friend>
)

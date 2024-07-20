package com.example.code_n_share_mobile.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.code_n_share_mobile.R
import com.example.code_n_share_mobile.models.Post
import org.koin.core.component.KoinComponent

class PostAdapter(
    private var posts: List<Post>,
    private val userId: String,
    private val onDeletePost: (String, String) -> Unit,
    private val onLikePost: (String) -> Unit,
    private val onUnlikePost: (String) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>(), KoinComponent {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view, userId, onDeletePost, onLikePost, onUnlikePost)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    fun updatePosts(newPosts: List<Post>) {
        posts = newPosts
        notifyDataSetChanged()
    }

    override fun getItemCount() = posts.size

    class PostViewHolder(
        itemView: View,
        private val userId: String,
        private val onDeletePost: (String, String) -> Unit,
        private val onLikePost: (String) -> Unit,
        private val onUnlikePost: (String) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val imgProfile: ImageView = itemView.findViewById(R.id.img_profile)
        private val tvUsername: TextView = itemView.findViewById(R.id.tv_username)
        private val tvHandle: TextView = itemView.findViewById(R.id.tv_handle)
        private val tvContent: TextView = itemView.findViewById(R.id.tv_content)
        private val imgPostImage: ImageView = itemView.findViewById(R.id.img_post_image)
        private val btnDeletePost: ImageView = itemView.findViewById(R.id.btn_delete_post)
        private val btnLikePost: ImageView = itemView.findViewById(R.id.btn_like_post)
        private val tvLikesCount: TextView = itemView.findViewById(R.id.tv_likes_count)

        fun bind(post: Post) {
            tvUsername.text = "${post.author.firstname} ${post.author.lastname}"
            tvHandle.text = "@${post.author.email.split("@")[0]}"
            tvContent.text = post.content
            tvLikesCount.text = post.likesCount.toString()

            Glide.with(itemView.context)
                .load(post.author.avatar)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(imgProfile)

            if (!post.image.isNullOrEmpty()) {
                imgPostImage.visibility = View.VISIBLE
                Glide.with(itemView.context)
                    .load(post.image)
                    .into(imgPostImage)
            } else {
                imgPostImage.visibility = View.GONE
            }

            if (post.author.userId == userId) {
                btnDeletePost.visibility = View.VISIBLE
                btnDeletePost.setOnClickListener {
                    Log.d("PostAdapter", "Delete post clicked for postId: ${post.postId}")
                    onDeletePost(post.postId, userId)
                }
            } else {
                btnDeletePost.visibility = View.GONE
            }

            btnLikePost.setImageResource(if (post.isLikedByUser) R.drawable.ic_liked else R.drawable.ic_like)
            btnLikePost.setOnClickListener {
                if (post.isLikedByUser) {
                    onUnlikePost(post.postId)
                    post.isLikedByUser = false
                    post.likesCount -= 1
                    btnLikePost.setImageResource(R.drawable.ic_like)
                } else {
                    onLikePost(post.postId)
                    post.isLikedByUser = true
                    post.likesCount += 1
                    btnLikePost.setImageResource(R.drawable.ic_liked)
                }
                tvLikesCount.text = post.likesCount.toString()
            }
        }
    }
}

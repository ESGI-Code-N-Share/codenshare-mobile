package com.example.code_n_share_mobile.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.code_n_share_mobile.R
import com.example.code_n_share_mobile.models.User

class UserAdapter(
    private var users: List<User>,
    private val onUserClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemUserImage: ImageView = itemView.findViewById(R.id.item_user_image)
        val itemUserName: TextView = itemView.findViewById(R.id.item_user_name)
        val itemUserHandle: TextView = itemView.findViewById(R.id.item_user_handle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_user_search, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.itemUserName.text = "${user.firstname} ${user.lastname}"
        holder.itemUserHandle.text = "@${user.email.split("@")[0]}"
        Glide.with(holder.itemView.context)
            .load(user.avatar)
            .into(holder.itemUserImage)

        Log.d("UserAdapter", "Binding user: ${user.firstname} ${user.lastname}")

        holder.itemView.setOnClickListener {
            Log.d("UserAdapter", "User clicked: ${user.firstname} ${user.lastname}")
            onUserClick(user)
        }
    }

    override fun getItemCount(): Int {
        Log.d("UserAdapter", "Total users: ${users.size}")
        return users.size
    }

    fun updateUsers(newUsers: List<User>) {
        users = newUsers
        notifyDataSetChanged()
    }
}

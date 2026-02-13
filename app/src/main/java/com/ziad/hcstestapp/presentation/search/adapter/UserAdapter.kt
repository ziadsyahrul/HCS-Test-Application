package com.ziad.hcstestapp.presentation.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.ziad.hcstestapp.R
import com.ziad.hcstestapp.databinding.ItemUserBinding
import com.ziad.hcstestapp.domain.model.GithubUser

class UserAdapter(
    private val onUserClick: (GithubUser) -> Unit
) : ListAdapter<GithubUser, UserAdapter.UserViewHolder>(UserDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserAdapter.UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class UserViewHolder(
        private val binding: ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: GithubUser) {
            binding.apply {
                tvUsername.text = user.login
                tvUserType.text = user.type

                Glide.with(ivAvatar.context)
                    .load(user.avatarUrl)
                    .transform(CircleCrop())
                    .placeholder(R.drawable.ic_placeholder_user)
                    .error(R.drawable.ic_placeholder_user)
                    .into(ivAvatar)

                root.setOnClickListener {
                    onUserClick(user)
                }
            }
        }
    }

    private class UserDiffCallback : DiffUtil.ItemCallback<GithubUser>() {
        override fun areItemsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GithubUser, newItem: GithubUser): Boolean {
            return oldItem == newItem

        }
    }
}
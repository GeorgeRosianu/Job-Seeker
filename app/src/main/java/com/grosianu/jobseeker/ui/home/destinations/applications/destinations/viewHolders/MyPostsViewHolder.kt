package com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.grosianu.jobseeker.databinding.ItemPostBinding
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.adapters.MyPostsAdapter

class MyPostsViewHolder(
    val binding: ItemPostBinding,
    listener: MyPostsAdapter.MyPostsAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.run {
            this.listener = listener
        }
    }

    fun bind(post: Post) {
        binding.application = post
        binding.executePendingBindings()
    }
}
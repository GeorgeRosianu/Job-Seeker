package com.grosianu.jobseeker.ui.home.destinations.home

import androidx.recyclerview.widget.RecyclerView
import com.grosianu.jobseeker.databinding.ItemPostHomeBinding
import com.grosianu.jobseeker.models.Application

class PostsViewHolder(
    val binding: ItemPostHomeBinding,
    listener: PostsAdapter.PostsAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.run {
            this.listener = listener
        }
    }

    fun bind(application: Application) {
        binding.application = application
        binding.executePendingBindings()
    }
}
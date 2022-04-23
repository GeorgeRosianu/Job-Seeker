package com.grosianu.jobseeker.ui.home.destinations.home.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.grosianu.jobseeker.databinding.ItemApplicationHomeBinding
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.ui.home.destinations.home.adapters.ApplicationsAdapter

class ApplicationsViewHolder(
    val binding: ItemApplicationHomeBinding,
    listener: ApplicationsAdapter.ApplicationsAdapterListener
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
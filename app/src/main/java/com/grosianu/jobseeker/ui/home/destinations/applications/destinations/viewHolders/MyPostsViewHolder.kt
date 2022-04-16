package com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.grosianu.jobseeker.databinding.ItemOfferBinding
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.adapters.MyPostsAdapter

class MyPostsViewHolder(
    val binding: ItemOfferBinding,
    listener: MyPostsAdapter.MyPostsAdapterListener
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
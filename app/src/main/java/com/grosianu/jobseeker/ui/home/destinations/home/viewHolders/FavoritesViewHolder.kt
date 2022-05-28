package com.grosianu.jobseeker.ui.home.destinations.home.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.grosianu.jobseeker.databinding.ItemFavoritesHomeBinding
import com.grosianu.jobseeker.databinding.ItemPostBinding
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.ui.home.destinations.home.adapters.FavoritesAdapter

class FavoritesViewHolder(
    val binding: ItemFavoritesHomeBinding,
    listener: FavoritesAdapter.FavoritesAdapterListener
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
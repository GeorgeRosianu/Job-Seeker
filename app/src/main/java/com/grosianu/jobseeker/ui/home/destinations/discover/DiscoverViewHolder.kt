package com.grosianu.jobseeker.ui.home.destinations.discover

import androidx.recyclerview.widget.RecyclerView
import com.grosianu.jobseeker.databinding.ItemPostDiscoverBinding
import com.grosianu.jobseeker.models.Application

class DiscoverViewHolder(
    val binding: ItemPostDiscoverBinding,
    listener: DiscoverAdapter.DiscoverAdapterListener
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
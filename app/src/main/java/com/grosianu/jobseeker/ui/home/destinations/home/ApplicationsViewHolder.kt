package com.grosianu.jobseeker.ui.home.destinations.home

import androidx.recyclerview.widget.RecyclerView
import com.grosianu.jobseeker.databinding.ItemApplicationHomeBinding
import com.grosianu.jobseeker.models.Application

class ApplicationsViewHolder(
    val binding: ItemApplicationHomeBinding,
    listener: ApplicationsAdapter.ApplicationsAdapterListener
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
package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import androidx.recyclerview.widget.RecyclerView
import com.grosianu.jobseeker.databinding.ItemApplicationBinding
import com.grosianu.jobseeker.models.Application

class MyApplicationsViewHolder(
    val binding: ItemApplicationBinding,
    listener: MyApplicationsAdapter.MyApplicationsAdapterListener
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
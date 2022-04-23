package com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.grosianu.jobseeker.databinding.ItemApplicantBinding
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.models.User
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.adapters.ApplicantsAdapter

class ApplicantsViewHolder(
    val binding: ItemApplicantBinding,
    listener: ApplicantsAdapter.ApplicantsAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.listener = listener
    }

    fun bind(application: Application) {
        binding.application = application
        binding.executePendingBindings()
    }
}
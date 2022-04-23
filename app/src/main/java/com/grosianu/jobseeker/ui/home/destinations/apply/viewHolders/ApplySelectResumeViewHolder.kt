package com.grosianu.jobseeker.ui.home.destinations.apply.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.grosianu.jobseeker.databinding.ItemApplySelectResumeBinding
import com.grosianu.jobseeker.models.Resume
import com.grosianu.jobseeker.ui.home.destinations.apply.adapters.ApplySelectResumeAdapter

class ApplySelectResumeViewHolder(
    val binding: ItemApplySelectResumeBinding,
    listener: ApplySelectResumeAdapter.ApplySelectResumeAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.listener = listener
    }

    fun bind(resume: Resume) {
        binding.file = resume
        binding.executePendingBindings()
    }
}
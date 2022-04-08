package com.grosianu.jobseeker.ui.home.destinations.resume

import androidx.recyclerview.widget.RecyclerView
import com.grosianu.jobseeker.databinding.ResumeCardLayoutBinding
import com.grosianu.jobseeker.models.Resume

class ResumeViewHolder(
    val binding: ResumeCardLayoutBinding,
    listener: ResumeAdapter.ResumeAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.listener = listener
    }

    fun bind(resume: Resume) {
        binding.file = resume
        binding.executePendingBindings()
    }
}
package com.grosianu.jobseeker.ui.home.destinations.discover

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.ItemPostDiscoverBinding
import com.grosianu.jobseeker.models.Application

class DiscoverViewHolder(
    val binding: ItemPostDiscoverBinding,
    listener: DiscoverAdapter.DiscoverAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.listener = listener
    }

    fun bind(application: Application) {
        binding.application = application
        binding.applyBtn.setOnClickListener { setButton(application) }
        binding.executePendingBindings()
    }

    fun setButton(application: Application) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (application.applicants?.contains(currentUser?.uid) == true) {
            binding.applyBtn.run {
                isEnabled = false
                isClickable = false
                text = "Applied"
                alpha = 0.5F
            }
        }
    }
}
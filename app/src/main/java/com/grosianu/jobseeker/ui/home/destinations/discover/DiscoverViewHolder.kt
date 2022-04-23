package com.grosianu.jobseeker.ui.home.destinations.discover

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.grosianu.jobseeker.databinding.ItemPostDiscoverBinding
import com.grosianu.jobseeker.models.Post

class DiscoverViewHolder(
    val binding: ItemPostDiscoverBinding,
    listener: DiscoverAdapter.DiscoverAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.listener = listener
    }

    fun bind(post: Post) {
        binding.application = post
        binding.applyBtn.setOnClickListener { setButton(post) }
        binding.executePendingBindings()
    }

    fun setButton(post: Post) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (post.applicants?.contains(currentUser?.uid) == true) {
            binding.applyBtn.run {
                isEnabled = false
                isClickable = false
                text = "Applied"
                alpha = 0.5F
            }
        }
    }
}
package com.grosianu.jobseeker.ui.home.destinations.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grosianu.jobseeker.databinding.ItemApplicationHomeBinding
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.ui.home.destinations.home.viewHolders.ApplicationsViewHolder

class ApplicationsAdapter(
    private val listener: ApplicationsAdapterListener
) : ListAdapter<Post, ApplicationsViewHolder>(DiffCallback) {

    interface ApplicationsAdapterListener {
        fun onApplicationClicked(cardView: View, post: Post)
        fun onApplicationLongPressed(post: Post): Boolean
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.owner == newItem.owner &&
                    oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.industry == newItem.industry &&
                    oldItem.salary == newItem.salary &&
                    oldItem.company == newItem.company &&
                    oldItem.location == newItem.location &&
                    oldItem.description == newItem.description &&
                    oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ApplicationsViewHolder(
            ItemApplicationHomeBinding.inflate(layoutInflater, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: ApplicationsViewHolder, position: Int) {
        val application = getItem(position)
        holder.bind(application)
    }
}
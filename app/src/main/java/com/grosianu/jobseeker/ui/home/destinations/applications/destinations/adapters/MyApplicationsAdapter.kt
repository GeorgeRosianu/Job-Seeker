package com.grosianu.jobseeker.ui.home.destinations.applications.destinations.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grosianu.jobseeker.databinding.ItemApplicationBinding
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewHolders.MyApplicationsViewHolder

class MyApplicationsAdapter(
    private val listener: MyApplicationsAdapterListener
) : ListAdapter<Post, MyApplicationsViewHolder>(DiffCallback) {

    interface MyApplicationsAdapterListener {
        fun onPostClicked(cardView: View, post: Post)
        fun onPostLongPressed(post: Post): Boolean
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.title == newItem.title && oldItem.owner == newItem.owner
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.industry == newItem.industry &&
                    oldItem.salary == newItem.salary &&
                    oldItem.company == newItem.company &&
                    oldItem.location == newItem.location &&
                    oldItem.description == newItem.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyApplicationsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyApplicationsViewHolder(
            ItemApplicationBinding.inflate(layoutInflater, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: MyApplicationsViewHolder, position: Int) {
        val application = getItem(position)
        holder.bind(application)
    }
}
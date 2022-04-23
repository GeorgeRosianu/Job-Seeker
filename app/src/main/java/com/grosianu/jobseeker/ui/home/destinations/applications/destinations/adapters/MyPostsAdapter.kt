package com.grosianu.jobseeker.ui.home.destinations.applications.destinations.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grosianu.jobseeker.databinding.ItemPostBinding
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewHolders.MyPostsViewHolder

class MyPostsAdapter(
    private val listener: MyPostsAdapterListener
) : ListAdapter<Post, MyPostsViewHolder>(DiffCallback) {

    interface MyPostsAdapterListener {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyPostsViewHolder(
            ItemPostBinding.inflate(layoutInflater, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: MyPostsViewHolder, position: Int) {
        val application = getItem(position)
        holder.bind(application)
    }
}
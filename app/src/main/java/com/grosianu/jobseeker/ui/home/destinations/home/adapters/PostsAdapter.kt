package com.grosianu.jobseeker.ui.home.destinations.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grosianu.jobseeker.databinding.ItemPostHomeBinding
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.ui.home.destinations.home.viewHolders.PostsViewHolder

class PostsAdapter(
    private val listener: PostsAdapterListener
) : ListAdapter<Post, PostsViewHolder>(DiffCallback) {

    interface PostsAdapterListener {
        fun onPostClicked(cardView: View, post: Post)
        fun onPostLongPressed(post: Post): Boolean
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PostsViewHolder(
            ItemPostHomeBinding.inflate(layoutInflater, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        val application = getItem(position)
        holder.bind(application)
    }
}
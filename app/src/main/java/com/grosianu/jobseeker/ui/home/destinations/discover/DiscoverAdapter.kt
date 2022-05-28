package com.grosianu.jobseeker.ui.home.destinations.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.checkbox.MaterialCheckBox
import com.grosianu.jobseeker.databinding.ItemPostDiscoverBinding
import com.grosianu.jobseeker.models.Post

class DiscoverAdapter(
    private val listener: DiscoverAdapterListener
) : ListAdapter<Post, DiscoverViewHolder>(DiffCallback) {

    interface DiscoverAdapterListener {
        fun onPostClicked(cardView: View, post: Post)
        fun onPostLongPressed(post: Post): Boolean
        fun onApplyClicked(post: Post)
        fun onAddFavoriteClicked(view: View, post: Post)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DiscoverViewHolder(
            ItemPostDiscoverBinding.inflate(layoutInflater, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: DiscoverViewHolder, position: Int) {
        val application = getItem(position)
        holder.bind(application)
        holder.setButton(application)
        holder.setFavorite(application)
    }
}
package com.grosianu.jobseeker.ui.home.destinations.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grosianu.jobseeker.databinding.ItemPostHomeBinding
import com.grosianu.jobseeker.models.Application

class PostsAdapter(
    private val listener: PostsAdapterListener
) : ListAdapter<Application, PostsViewHolder>(PostsAdapter) {

    interface PostsAdapterListener {
        fun onPostClicked(cardView: View, application: Application)
        fun onPostLongPressed(application: Application): Boolean
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Application>() {
        override fun areItemsTheSame(oldItem: Application, newItem: Application): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.owner == newItem.owner &&
                    oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Application, newItem: Application): Boolean {
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
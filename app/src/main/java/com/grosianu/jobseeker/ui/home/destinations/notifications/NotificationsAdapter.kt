package com.grosianu.jobseeker.ui.home.destinations.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grosianu.jobseeker.databinding.ItemNewsBinding
import com.grosianu.jobseeker.models.News

class NotificationsAdapter(
    private val listener: NotificationsAdapterListener
) : ListAdapter<News, NotificationsViewHolder>(DiffCallback) {

    interface NotificationsAdapterListener {
        fun onItemClicked(cardView: View, item: News)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.message == newItem.message &&
                    oldItem.timestamp == newItem.timestamp &&
                    oldItem.type == newItem.type &&
                    oldItem.userId == newItem.userId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return NotificationsViewHolder(
            ItemNewsBinding.inflate(layoutInflater, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)
    }
}
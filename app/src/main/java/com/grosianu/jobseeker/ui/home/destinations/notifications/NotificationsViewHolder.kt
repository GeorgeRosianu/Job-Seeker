package com.grosianu.jobseeker.ui.home.destinations.notifications

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.grosianu.jobseeker.databinding.ItemNewsBinding
import com.grosianu.jobseeker.models.News

class NotificationsViewHolder(
    val binding: ItemNewsBinding,
    listener: NotificationsAdapter.NotificationsAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.listener = listener
    }

    fun bind(news: News) {
        binding.news = news
        binding.newIcon.isVisible = !news.seen
        binding.executePendingBindings()
    }
}
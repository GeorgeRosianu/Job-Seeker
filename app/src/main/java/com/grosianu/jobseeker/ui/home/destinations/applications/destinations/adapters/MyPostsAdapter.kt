package com.grosianu.jobseeker.ui.home.destinations.applications.destinations.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grosianu.jobseeker.databinding.ItemOfferBinding
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewHolders.MyPostsViewHolder

class MyPostsAdapter(
    private val listener: MyPostsAdapterListener
) : ListAdapter<Application, MyPostsViewHolder>(DiffCallback) {

    interface MyPostsAdapterListener {
        fun onPostClicked(cardView: View, application: Application)
        fun onPostLongPressed(application: Application): Boolean
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Application>() {
        override fun areItemsTheSame(oldItem: Application, newItem: Application): Boolean {
            return oldItem.title == newItem.title && oldItem.owner == newItem.owner
        }

        override fun areContentsTheSame(oldItem: Application, newItem: Application): Boolean {
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
            ItemOfferBinding.inflate(layoutInflater, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: MyPostsViewHolder, position: Int) {
        val application = getItem(position)
        holder.bind(application)
    }
}
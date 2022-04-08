package com.grosianu.jobseeker.ui.home.destinations.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grosianu.jobseeker.databinding.ItemPostDiscoverBinding
import com.grosianu.jobseeker.models.Application

class DiscoverAdapter(
    private val listener: DiscoverAdapterListener
) : ListAdapter<Application, DiscoverViewHolder>(DiscoverAdapter) {

    interface DiscoverAdapterListener {
        fun onPostClicked(cardView: View, application: Application)
        fun onPostLongPressed(application: Application): Boolean
        fun onApplyClicked(application: Application)
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
    }
}
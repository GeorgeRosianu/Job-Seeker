package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grosianu.jobseeker.databinding.ItemApplicationBinding
import com.grosianu.jobseeker.models.Application

class MyApplicationsAdapter(
    private val listener: MyApplicationsAdapterListener
) : ListAdapter<Application, MyApplicationsViewHolder>(DiffCallback) {

    interface MyApplicationsAdapterListener {
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
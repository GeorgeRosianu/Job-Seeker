package com.grosianu.jobseeker.ui.home.destinations.applications.destinations.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.grosianu.jobseeker.databinding.ItemApplicantBinding
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.models.User
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewHolders.ApplicantsViewHolder

class ApplicantsAdapter(
    private val listener: ApplicantsAdapterListener
) : ListAdapter<Application, ApplicantsViewHolder>(DiffCallback) {

    interface ApplicantsAdapterListener {
        fun onApplicantClicked(cardView: View, application: Application)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Application>() {
        override fun areItemsTheSame(oldItem: Application, newItem: Application): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Application, newItem: Application): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.postId == newItem.postId &&
                    oldItem.applicantId == newItem.applicantId &&
                    oldItem.resumeId == newItem.resumeId
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicantsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ApplicantsViewHolder(
            ItemApplicantBinding.inflate(layoutInflater, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: ApplicantsViewHolder, position: Int) {
        val application = getItem(position)
        holder.bind(application)
    }
}
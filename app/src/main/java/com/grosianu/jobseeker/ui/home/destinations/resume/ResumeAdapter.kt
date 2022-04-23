package com.grosianu.jobseeker.ui.home.destinations.resume

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grosianu.jobseeker.databinding.ResumeCardLayoutBinding
import com.grosianu.jobseeker.models.Resume

class ResumeAdapter(
    private val listener: ResumeAdapterListener
) : ListAdapter<Resume, ResumeViewHolder>(DiffCallback) {

    interface ResumeAdapterListener {
        fun onResumeClicked(cardView: View, resume: Resume)
        fun onResumeLongPressed(resume: Resume): Boolean
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Resume>() {
        override fun areItemsTheSame(oldItem: Resume, newItem: Resume): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Resume, newItem: Resume): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.owner == newItem.owner &&
                    oldItem.dateCreated == newItem.dateCreated
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResumeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ResumeViewHolder(
            ResumeCardLayoutBinding.inflate(layoutInflater, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: ResumeViewHolder, position: Int) {
        val resume = getItem(position)
        holder.bind(resume)
    }
}
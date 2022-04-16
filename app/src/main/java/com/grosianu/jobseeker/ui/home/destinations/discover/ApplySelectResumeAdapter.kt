package com.grosianu.jobseeker.ui.home.destinations.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.grosianu.jobseeker.databinding.ItemApplySelectResumeBinding
import com.grosianu.jobseeker.models.Resume

class ApplySelectResumeAdapter(
    private val listener: ApplySelectResumeAdapterListener
) : ListAdapter<Resume, ApplySelectResumeViewHolder>(DiffCallback) {

    interface ApplySelectResumeAdapterListener {
        fun onResumeClicked(resume: Resume)
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

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplySelectResumeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ApplySelectResumeViewHolder(
            ItemApplySelectResumeBinding.inflate(layoutInflater, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: ApplySelectResumeViewHolder, position: Int) {
        val resume = getItem(position)
        holder.bind(resume)
    }
}
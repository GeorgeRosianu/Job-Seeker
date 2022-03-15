package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.ItemOfferBinding
import com.grosianu.jobseeker.databinding.LayoutDetailsBinding
import com.grosianu.jobseeker.models.Application

class MyPostsAdapter :
    ListAdapter<Application, MyPostsAdapter.MyPostsViewHolder>(DiffCallback) {

//        interface MyPostsAdapterListener {
//        fun onPostClicked(cardView: View, application: Application)
//        fun onPostLongPressed(application: Application): Boolean
//    }

    class MyPostsViewHolder(
        val binding: ItemOfferBinding
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(application: Application) {
                binding.application = application
                binding.executePendingBindings()
            }
        }

    companion object DiffCallback : DiffUtil.ItemCallback<Application>() {
        override fun areItemsTheSame(oldItem: Application, newItem: Application): Boolean {
            return oldItem.title == newItem.title && oldItem.owner == newItem.owner
        }

        override fun areContentsTheSame(oldItem: Application, newItem: Application): Boolean {
            return  oldItem.industry == newItem.industry &&
                    oldItem.salary == newItem.salary &&
                    oldItem.company == newItem.company &&
                    oldItem.location == newItem.location &&
                    oldItem.description == newItem.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPostsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return MyPostsViewHolder(
            ItemOfferBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyPostsViewHolder, position: Int) {
        val application = getItem(position)
        holder.bind(application)
    }
}

//class OfferListener(val clickListener: (application: Application) -> Unit) {
//    fun onClick(application: Application) = clickListener(application)

//}
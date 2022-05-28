package com.grosianu.jobseeker.ui.home.destinations.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.grosianu.jobseeker.databinding.ItemFavoritesHomeBinding
import com.grosianu.jobseeker.databinding.ItemPostBinding
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.ui.home.destinations.home.viewHolders.FavoritesViewHolder

class FavoritesAdapter(
    private val listener: FavoritesAdapterListener
) : ListAdapter<Post, FavoritesViewHolder>(DiffCallback) {

    interface FavoritesAdapterListener {
        fun onFavoriteClicked(cardView: View, post: Post)
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.title == newItem.title &&
                    oldItem.owner == newItem.owner &&
                    oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.industry == newItem.industry &&
                    oldItem.salary == newItem.salary &&
                    oldItem.company == newItem.company &&
                    oldItem.location == newItem.location &&
                    oldItem.description == newItem.description &&
                    oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FavoritesViewHolder(
            ItemFavoritesHomeBinding.inflate(layoutInflater, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}
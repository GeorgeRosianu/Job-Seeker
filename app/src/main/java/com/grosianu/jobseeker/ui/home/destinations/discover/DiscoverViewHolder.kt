package com.grosianu.jobseeker.ui.home.destinations.discover

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.databinding.ItemPostDiscoverBinding
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.models.User

class DiscoverViewHolder(
    val binding: ItemPostDiscoverBinding,
    listener: DiscoverAdapter.DiscoverAdapterListener
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.listener = listener
    }

    fun bind(post: Post) {
        binding.application = post
        binding.applyBtn.setOnClickListener { setButton(post) }
        binding.executePendingBindings()
    }

    fun setButton(post: Post) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (post.applicants?.contains(currentUser?.uid) == true) {
            binding.applyBtn.apply {
                isEnabled = false
                isClickable = false
                text = "Applied"
                alpha = 0.5F
            }
        }
    }

    fun setFavorite(post: Post) {
        val db = FirebaseFirestore.getInstance()
        val dbRef = db.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val user = document.toObject<User>()

                    if (user?.favorites != null && user.favorites.contains(post.id)) {
                        binding.favoriteBtn.isChecked = true
                    }
                }
            }
    }
}
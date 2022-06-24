package com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels

import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.models.User
import java.util.*

class CreateViewModel : ViewModel() {

    private var _post = MutableLiveData<Post>()
    val post: LiveData<Post> = _post

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun updateFirestoreData(post: Post) {
        if (post.image?.isEmpty() == true) {
            updatePostFirestoreData(post)
        } else {
            val storageReference = storage.getReference("images/${post.imageId}")

            storageReference.putFile(post.image?.toUri()!!)
                .addOnSuccessListener {
                    storageReference.downloadUrl.addOnSuccessListener {
                        val uri = it.toString()
                        post.image = uri
                        updatePostFirestoreData(post)
                    }
                }
        }
    }
    private fun updatePostFirestoreData(post: Post) {
        db.collection("posts").document(post.id!!)
            .set(post)
    }


    companion object {
        private const val TAG = "CREATE_VIEW_MODEL"
    }
}
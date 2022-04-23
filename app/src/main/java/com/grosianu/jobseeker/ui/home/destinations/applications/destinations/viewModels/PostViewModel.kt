package com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.grosianu.jobseeker.models.Post
import kotlinx.coroutines.launch

class PostViewModel : ViewModel() {

    private var _post = MutableLiveData<Post>()
    val post: LiveData<Post> = _post

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun getPost(postId: String) {
        viewModelScope.launch {
            val docRef = db.collection("posts").document(postId)
            docRef.addSnapshotListener(MetadataChanges.INCLUDE) { document, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (document != null && document.exists()) {
                        _post.value = document.toObject()
                    }
                }
            }
        }

    fun deletePost(postId: String, context: Context) {
        viewModelScope.launch {
            val docRef = db.collection("posts").document(postId)
            docRef.delete()
                .addOnSuccessListener {
                    Toast.makeText(context, "Post deleted", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Post could not be deleted", Toast.LENGTH_SHORT).show()
                }
        }
    }

        companion object {
            private const val TAG = "POST_VIEW_MODEL"
        }
    }
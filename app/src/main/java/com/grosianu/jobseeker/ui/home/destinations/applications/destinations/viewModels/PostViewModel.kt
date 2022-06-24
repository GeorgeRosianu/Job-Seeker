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
import com.grosianu.jobseeker.models.News
import com.grosianu.jobseeker.models.Post
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

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

    fun deletePost() {
        viewModelScope.launch {
            val storageRef = storage.reference.child("images/${_post.value?.imageId}")
            val postDocRef = db.collection("posts").document(_post.value?.id!!)
            val applicationsDocRef = db.collection("applications")

            println("###########################")
            println(_post.value?.imageId)
            println(_post.value?.id)

            storageRef.delete().addOnSuccessListener {
                println("TEST 1")
                applicationsDocRef.whereEqualTo("postId", _post.value?.id)
                println("TEST 2")
                applicationsDocRef.get().addOnSuccessListener { querySnapshot ->
                    querySnapshot.forEach { document ->
                        println("TEST 3")
                        document.reference.delete()
                    }
                }
                println("TEST 4")
                postDocRef.delete()
                println("TEST 5")
            }
        }
    }

        companion object {
            private const val TAG = "POST_VIEW_MODEL"
        }
    }
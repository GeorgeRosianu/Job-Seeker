package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.grosianu.jobseeker.models.Application
import kotlinx.coroutines.launch
import java.io.File

class MyPostsViewModel : ViewModel() {

    private var _posts = MutableLiveData<List<Application>>()
    val posts: LiveData<List<Application>> = _posts

    private val _post = MutableLiveData<Application>()
    val post: LiveData<Application> = _post

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun getPostList() {
        val postsTmp = mutableListOf<Application>()
        viewModelScope.launch {
            val docRef = db.collection("applications")
            docRef.whereEqualTo("owner", auth.currentUser?.uid)
                .get()
                .addOnCompleteListener {
                    for (document in it.result) {
                        val postTmp = document.toObject<Application>()
                        postsTmp.add(postTmp)
                        Log.d(TAG, "${document.id} => ${document.data}")
                    }
                    _posts.value = postsTmp
                }
                .addOnFailureListener { exception ->
                    _posts.value = listOf()
                    Log.w(TAG, "Error getting documents: ", exception)
                }

            docRef.whereEqualTo("owner", auth.currentUser?.uid)
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }

                val source = if (querySnapshot != null && querySnapshot.metadata.hasPendingWrites())
                    "Local"
                else
                    "Server"

                val applications = ArrayList<Application>()
                for (document in querySnapshot!!) {
                    if (document != null && document.exists()) {
                        applications.add(document.toObject<Application>())
                    }
                }
            }
        }
    }

//    fun onPostClicked(application: Application) {
//        _post.value = application
//    }

    companion object {
        private const val TAG = "MY_POSTS_VIEW_MODEL"
    }
}
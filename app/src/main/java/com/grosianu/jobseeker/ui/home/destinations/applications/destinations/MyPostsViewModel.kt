package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.models.Application
import kotlinx.coroutines.launch

class MyPostsViewModel : ViewModel() {

    private var _posts = MutableLiveData<List<Application>>()
    val posts: LiveData<List<Application>> = _posts

    private val _post = MutableLiveData<Application>()
    val post: LiveData<Application> = _post

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun getPostList() {
        viewModelScope.launch {
            val docRef = db.collection("applications")
            docRef.whereEqualTo("owner", auth.currentUser?.uid)
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    val applications = ArrayList<Application>()
                    for (document in querySnapshot!!) {
                        if (document != null && document.exists()) {
                            applications.add(document.toObject())
                        }
                    }
                    _posts.value = applications
                }
        }
    }

    companion object {
        private const val TAG = "MY_POSTS_VIEW_MODEL"
    }
}
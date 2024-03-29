package com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.models.Post
import kotlinx.coroutines.launch

class MyApplicationsViewModel : ViewModel() {

    private var _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private var _post = MutableLiveData<Post>()
    val post: LiveData<Post> = _post

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var _hasApplications = MutableLiveData(false)
    val hasApplications: LiveData<Boolean> = _hasApplications

    fun getPostList() {
        viewModelScope.launch {
            val docRef = db.collection("posts")
            docRef.whereArrayContains("applicants", auth.currentUser?.uid!!)
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    val applications = ArrayList<Post>()
                    if (querySnapshot != null) {
                        for (document in querySnapshot) {
                            if (document != null && document.exists()) {
                                applications.add(document.toObject())
                            }
                        }
                    }
                    _posts.value = applications
                    _hasApplications.value = !applications.isNullOrEmpty()
                }
        }
    }

    companion object {
        private const val TAG = "MY_APPLICATIONS_VIEW_MODEL"
    }
}
package com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.models.Post
import kotlinx.coroutines.launch

class MyPostsViewModel : ViewModel() {

    private var _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post> = _post

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var _hasPosts = MutableLiveData(false)
    val hasPosts: LiveData<Boolean> = _hasPosts

    fun getPostList() {
        viewModelScope.launch {
            val docRef = db.collection("posts")
            docRef.whereEqualTo("owner", auth.currentUser?.uid)
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    val applications = ArrayList<Post>()
                    for (document in querySnapshot!!) {
                        if (document != null && document.exists()) {
                            applications.add(document.toObject())
                        }
                    }
                    _posts.value = applications
                    _hasPosts.value = !applications.isNullOrEmpty()
                }
        }
    }

    companion object {
        private const val TAG = "MY_POSTS_VIEW_MODEL"
    }
}
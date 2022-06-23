package com.grosianu.jobseeker.ui.home.destinations.home.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.models.User
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private var _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

//    private var _post = MutableLiveData<Post>()
//    val post: LiveData<Post> = _post

    private var _applications = MutableLiveData<List<Post>>()
    val applications: LiveData<List<Post>> = _applications

//    private var _applicationInfo = MutableLiveData<Post>()
//    val applicationInfo: LiveData<Post> = _applicationInfo

    private var _favorites = MutableLiveData<List<Post>>()
    val favorites: LiveData<List<Post>> = _favorites

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    var hasPosts = MutableLiveData(false)
    var hasApplications = MutableLiveData(false)

    fun getPostList() {
        viewModelScope.launch {
            val docRef = db.collection("posts")
            docRef.whereEqualTo("owner", auth.currentUser?.uid)
                .limit(LIMIT.toLong())
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    val array = ArrayList<Post>()
                    for (document in querySnapshot!!) {
                        if (document != null && document.exists()) {
                            array.add(document.toObject())
                        }
                    }
                    _posts.value = array
                    hasPosts.value = !posts.value.isNullOrEmpty()
                }
        }
    }

    fun getApplicationList() {
        viewModelScope.launch {
            val docRef = db.collection("posts")
            docRef.whereArrayContains("applicants", auth.currentUser?.uid!!)
                .limit(LIMIT.toLong())
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    val array = ArrayList<Post>()
                    for (document in querySnapshot!!) {
                        if(document != null && document.exists()) {
                            array.add(document.toObject())
                            hasApplications.value = true
                        }
                    }
                    _applications.value = array
                    hasApplications.value = !applications.value.isNullOrEmpty()
                }
        }
    }

    fun getFavoritesList() {
        viewModelScope.launch {
            val docRef = db.collection("favorites")
            docRef.whereEqualTo("owner", auth.currentUser?.uid)
                .limit(LIMIT.toLong())
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }

                    val favList = ArrayList<Post>()
                    for (document in querySnapshot!!) {
                        if (document != null && document.exists()) {
                            favList.add(document.toObject())
                        }
                    }
                    _favorites.value = favList
                }
        }
    }

    companion object {
        private const val TAG = "HOME_VIEW_MODEL"
        private const val LIMIT = 5
    }
}
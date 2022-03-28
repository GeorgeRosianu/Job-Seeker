package com.grosianu.jobseeker.ui.home.destinations.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.models.Application
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

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
//            docRef.whereEqualTo("owner", auth.currentUser?.uid).limit(LIMIT.toLong())
//                .get()
//                .addOnCompleteListener {
//                    for (document in it.result) {
//                        val postTmp = document.toObject<Application>()
//                        postsTmp.add(postTmp)
//                    }
//                    _posts.value = postsTmp
//                }
//                .addOnFailureListener {
//                    _posts.value = listOf()
//                }

            docRef.whereEqualTo("owner", auth.currentUser?.uid)
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }

                    val applications = ArrayList<Application>()
                    for (document in querySnapshot!!) {
                        if (document != null && document.exists()) {
                            applications.add(document.toObject<Application>())
                        }
                    }
                    _posts.value = applications
                }
        }
    }

    companion object {
        private const val TAG = "HOME_VIEW_MODEL"
        private const val LIMIT = 5
    }
}
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

    private var _post = MutableLiveData<Application>()
    val post: LiveData<Application> = _post

    private var _applications = MutableLiveData<List<Application>>()
    val applications: LiveData<List<Application>> = _applications

    private var _application = MutableLiveData<Application>()
    val application: LiveData<Application> = _application

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun getPostList() {
        viewModelScope.launch {
            val docRef = db.collection("applications")
            docRef.whereEqualTo("owner", auth.currentUser?.uid)
                .limit(LIMIT.toLong())
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

    fun getApplicationList() {
        viewModelScope.launch {
            val docRef = db.collection("applications")
            docRef.whereArrayContains("applicants", auth.currentUser?.uid!!)
                .limit(LIMIT.toLong())
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }
                    val applications = ArrayList<Application>()
                    for (document in querySnapshot!!) {
                        if(document != null && document.exists()) {
                            applications.add(document.toObject())
                        }
                    }
                    _applications.value = applications
                }
        }
    }

    companion object {
        private const val TAG = "HOME_VIEW_MODEL"
        private const val LIMIT = 5
    }
}
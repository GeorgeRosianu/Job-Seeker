package com.grosianu.jobseeker.ui.home.destinations

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.models.Application
import kotlinx.coroutines.launch

class ApplicationViewModel : ViewModel() {

    private var _post = MutableLiveData<Application>()
    val post: LiveData<Application> = _post

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun getPost(postId: String) {
        viewModelScope.launch {
            val docRef = db.collection("applications").document(postId)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        _post.value = document.toObject<Application>()
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w(TAG, "Error getting documents: ", exception)
                }
        }
    }

    companion object {
        private const val TAG = "APPLICATION_VIEW_MODEL"
    }
}
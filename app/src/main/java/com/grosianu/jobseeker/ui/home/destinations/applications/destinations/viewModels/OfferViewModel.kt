package com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.models.User
import kotlinx.coroutines.launch

class OfferViewModel : ViewModel() {

    private var _post = MutableLiveData<Post>()
    val post: LiveData<Post> = _post

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var _isUserSetUp = false
    val isUserSetUp get() = _isUserSetUp

    fun isUserSetUp() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid.toString()
            val docRef = db.collection("users").document(userId)
            docRef.get()
                .addOnSuccessListener { document ->
                    var user: User? = null
                    if (document != null) {
                        user = document.toObject<User>()

                        if(!user?.displayName.isNullOrEmpty()) {
                            _isUserSetUp = true
                        }
                    }
                }
        }
    }

    fun getPost(postId: String) {
        viewModelScope.launch {
            val docRef = db.collection("posts").document(postId)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        _post.value = document.toObject<Post>()
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
        private const val TAG = "OFFER_VIEW_MODEL"
    }
}
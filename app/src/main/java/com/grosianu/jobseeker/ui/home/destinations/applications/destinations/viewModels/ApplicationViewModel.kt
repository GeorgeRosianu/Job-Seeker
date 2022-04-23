package com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.models.Post
import kotlinx.coroutines.launch

class ApplicationViewModel : ViewModel() {

    private var _post = MutableLiveData<Post>()
    val post: LiveData<Post> = _post

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

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

    fun userRemoveApplicant(documentId: String) {
        viewModelScope.launch {
            val postDocRef = db.collection("posts").document(documentId)
            postDocRef.update("applicants", FieldValue.arrayRemove(auth.currentUser?.uid))

            val applicationDocRef = db.collection("applications")
            applicationDocRef
                .whereEqualTo("postId", post.value?.id)
                .whereEqualTo("applicantId", auth.currentUser?.uid)
                .get()
                .addOnCompleteListener {
                    val result = it.result
                    val applicationId = result.documents[0].toObject<Application>()?.id.toString()

                    val docRef = db.collection("applications").document(applicationId)
                    docRef.delete()
                }
        }
    }

    companion object {
        private const val TAG = "APPLICATION_VIEW_MODEL"
    }
}
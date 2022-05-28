package com.grosianu.jobseeker.ui.home.destinations.apply.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.grosianu.jobseeker.models.Resume
import com.grosianu.jobseeker.models.User
import kotlinx.coroutines.launch

class ApplySelectResumeViewModel : ViewModel() {

    private var _resumes = MutableLiveData<List<Resume>>()
    val resumes: LiveData<List<Resume>> = _resumes

    private var _resume = MutableLiveData<Resume>()
    val resume: LiveData<Resume> = _resume

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private var isUserSetUp = false

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
                            isUserSetUp = true
                        }
                    }
                }
        }
    }

    fun getResumeList() {
        viewModelScope.launch {
            val docRef = db.collection("resumes")
            docRef.whereEqualTo("owner", auth.currentUser?.uid)
                .addSnapshotListener(MetadataChanges.INCLUDE) { querySnapshot, e ->
                    if (e != null) {
                        return@addSnapshotListener
                    }

                    val resumes = ArrayList<Resume>()
                    for (document in querySnapshot!!) {
                        if (document != null && document.exists()) {
                            resumes.add(document.toObject())
                        }
                    }
                    _resumes.value = resumes
                }
        }
    }

    companion object {
        private const val TAG = "APPLY_VIEW_MODEL"
    }
}
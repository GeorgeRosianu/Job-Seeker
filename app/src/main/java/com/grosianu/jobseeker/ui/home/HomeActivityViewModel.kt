package com.grosianu.jobseeker.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.models.Resume
import com.grosianu.jobseeker.models.User
import kotlinx.coroutines.launch

class HomeActivityViewModel : ViewModel() {

    private var _currentAccount = MutableLiveData<User>()
    val currentAccount: LiveData<User> = _currentAccount

    private var _resumes = MutableLiveData<List<Resume>>()
    val resumes: LiveData<List<Resume>> = _resumes

    private var _isUserSetUp = MutableLiveData(false)
    val isUserSetUp: LiveData<Boolean> = _isUserSetUp

    private var _hasResumes = MutableLiveData(false)
    val hasResumes: LiveData<Boolean> = _hasResumes

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    init {
        getUserData()
    }

    fun getUserData() {
        val userId = auth.currentUser?.uid.toString()
        val docRef = db.collection("users").document(userId)

        viewModelScope.launch {
            docRef.addSnapshotListener(MetadataChanges.INCLUDE) { document, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if(document != null) {
                    _currentAccount.value = document.toObject<User>()
                    isUserSetUp()
                }
            }
        }
    }

    private fun isUserSetUp() {
        viewModelScope.launch {
            val username = currentAccount.value?.displayName
            _isUserSetUp.value = !(username.isNullOrEmpty() || username == "User")
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

                    val resumeList = ArrayList<Resume>()
                    for (document in querySnapshot!!) {
                        if (document != null && document.exists()) {
                            resumeList.add(document.toObject())
                            _hasResumes.value = true
                        }
                    }

                    if (resumeList.isEmpty()) {
                        _resumes.value = listOf()
                        _hasResumes.value = false
                    }
                }
        }
    }

    fun signUserOut() {
        auth.signOut()
    }
}
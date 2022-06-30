package com.grosianu.jobseeker.ui.home.destinations.account.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.models.User
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel() {

    private var _currentAccount = MutableLiveData<User>()
    val currentAccount: LiveData<User> = _currentAccount

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun getUserData() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid.toString()
            val docRef = db.collection("users").document(userId)
            docRef.addSnapshotListener(MetadataChanges.INCLUDE) { document, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if(document != null) {
                    _currentAccount.value = document.toObject<User>()
                }
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
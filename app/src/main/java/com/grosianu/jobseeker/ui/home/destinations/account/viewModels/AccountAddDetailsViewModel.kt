package com.grosianu.jobseeker.ui.home.destinations.account.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.grosianu.jobseeker.models.User
import kotlinx.coroutines.launch

class AccountAddDetailsViewModel : ViewModel() {

    private var _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun getUserData() {
        viewModelScope.launch {
            val userId = auth.currentUser?.uid.toString()
            val docRef = db.collection("users").document(userId)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        _user.value = document.toObject<User>()
                    }
                }
        }
    }

    fun updateUserData(user: User) {
        _user.value = user
    }

    fun uploadUserData() {
        val docRef = db.collection("users").document(auth.currentUser?.uid.toString())
        docRef.set(user)
    }
}
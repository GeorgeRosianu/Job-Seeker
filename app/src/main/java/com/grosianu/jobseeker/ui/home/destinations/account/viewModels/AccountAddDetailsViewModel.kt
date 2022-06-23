package com.grosianu.jobseeker.ui.home.destinations.account.viewModels

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.models.User
import kotlinx.coroutines.launch
import java.util.*

class AccountAddDetailsViewModel : ViewModel() {

    private var _currentAccount = MutableLiveData<User>()
    val currentAccount: LiveData<User> = _currentAccount

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun updateFirestoreData(updatedUser: User) {
        if (updatedUser.imageUri.isEmpty()) {
            updateUserFirestoreData(updatedUser, "")
        } else {
            val fileName = UUID.randomUUID().toString()
            val storageReference = storage.getReference("images/$fileName")

            storageReference.putFile(updatedUser.imageUri.toUri())
                .addOnSuccessListener {
                    storageReference.downloadUrl.addOnSuccessListener {
                        updateUserFirestoreData(updatedUser, it.toString())
                    }
                }
        }
    }

    private fun updateUserFirestoreData(updatedUser: User, image: String) {
        val dbRef = db.collection("users").document(auth.currentUser?.uid!!)

        val displayName = "${updatedUser.firstName} ${updatedUser.lastName}"

        dbRef.update("firstName", updatedUser.firstName)
        dbRef.update("lastName", updatedUser.lastName)
        dbRef.update("phoneNumber", updatedUser.phoneNumber)
        dbRef.update("age", updatedUser.age)
        dbRef.update("residence", updatedUser.residence)
        dbRef.update("sex", updatedUser.sex)
        dbRef.update("educationLevel", updatedUser.educationLevel)
        dbRef.update("educationSpec", updatedUser.educationSpec)
        dbRef.update("educationCity", updatedUser.educationCity)
        dbRef.update("educationInstitution", updatedUser.educationInstitution)
        dbRef.update("educationDate", updatedUser.educationDate)
        dbRef.update("experiencePosition", updatedUser.experiencePosition)
        dbRef.update("experienceCompany", updatedUser.experienceCompany)
        dbRef.update("experienceCity", updatedUser.experienceCity)
        dbRef.update("experienceIndustry", updatedUser.experienceIndustry)
        dbRef.update("experienceYears", updatedUser.experienceYears)
        dbRef.update("experienceDescription", updatedUser.experienceDescription)
        if(updatedUser.firstName.isNotEmpty()) {
            dbRef.update("displayName", displayName)
        }
        if (image.isNotEmpty()) {
            dbRef.update("imageUri", image)
        }
    }
}
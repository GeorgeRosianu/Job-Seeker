package com.grosianu.jobseeker.ui.home.destinations.apply.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.models.Resume
import kotlinx.coroutines.launch
import java.util.*

class ApplyCheckDetailsViewModel : ViewModel() {

    private var _application = MutableLiveData<Application>()
    val application: LiveData<Application> = _application

    private var _resume = MutableLiveData<Resume>()
    val resume: LiveData<Resume> = _resume

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun createApplication(postId: String, resumeId: String, message: String) {
        val id: String = UUID.randomUUID().toString()
        val applicantId: String = auth.currentUser?.uid.toString()
        val applicantName: String = auth.currentUser?.displayName.toString()
        val applicantImageUrl: String = auth.currentUser?.photoUrl.toString()
        val applicantEmail: String = auth.currentUser?.email.toString()

        _application.value = Application(
            id,
            applicantId,
            applicantName,
            applicantImageUrl,
            applicantEmail,
            postId,
            resumeId,
            message
        )
    }

    fun uploadApplication(context: Context, application: Application) {
        viewModelScope.launch {
            db.collection("applications").document(application.id.toString())
                .set(application)
                .addOnCompleteListener {

                }
                .addOnFailureListener {
                    Toast.makeText(context, "Failed to create application", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    fun userAddApplicant(documentId: String) {
        viewModelScope.launch {
            val docRef = db.collection("posts").document(documentId)
            docRef.update("applicants", FieldValue.arrayUnion(auth.currentUser?.uid))
        }
    }

    fun loadPdfFile(resumeId: String) {
        viewModelScope.launch {
            val docRef = db.collection("resumes").document(resumeId)
            docRef.addSnapshotListener(MetadataChanges.INCLUDE) { document, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (document != null && document.exists()) {
                    _resume.value = document.toObject()
                }
            }
        }
    }
}
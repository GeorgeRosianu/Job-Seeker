package com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.models.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.*

class ApplicantViewModel : ViewModel() {

    private var _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    private var _resume = MutableLiveData<Resume>()
    val resume: LiveData<Resume> = _resume

    private var _application = MutableLiveData<Application>()
    val application: LiveData<Application> = _application

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    var hasStudies = MutableLiveData(false)
    var hasExperience = MutableLiveData(false)

    fun getUserData(userId: String) {
        viewModelScope.launch {
            val docRef = db.collection("users").document(userId)
            docRef.addSnapshotListener(MetadataChanges.INCLUDE) { document, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                if (document != null && document.exists()) {
                    _user.value = document.toObject()

                    if (!user.value?.educationLevel.isNullOrEmpty()) {
                        hasStudies.value = true
                    }
                    if (!user.value?.experiencePosition.isNullOrEmpty()) {
                        hasExperience.value = true
                    }
                }
            }
        }
    }

    fun getUserApplication(applicationId: String) {
        val docRef = db.collection("applications").document(applicationId)
        docRef.addSnapshotListener(MetadataChanges.INCLUDE) { document, e ->
            if (e != null) {
                return@addSnapshotListener
            }

            if (document != null && document.exists()) {
                _application.value = document.toObject()

                docRef.update("seen", true)

                getUserResume(application.value?.resumeId.toString())
            }
        }
    }

    private fun getUserResume(resumeId: String) {
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

    fun confirmApplicant() {
        viewModelScope.launch {
            val docRef = db.collection("posts").document(application.value?.postId!!)
            docRef.update("confirmedApplicants", FieldValue.arrayUnion(user.value?.userId))

            val appDocRef = db.collection("applications").document(application.value?.id!!)
            appDocRef.update("confirmed", true)

            val timestamp = LocalDateTime.now()
            val formattedDate = "${timestamp.dayOfMonth} ${timestamp.month}, ${timestamp.year}"

            val news = News(
                id = UUID.randomUUID().toString(),
                userId = application.value?.applicantId,
                title = "Your application has been confirmed!",
                message = "Someone will contact you in the following days to set up an interview.",
                seen = false,
                type = "APPLICATION_CONFIRMATION",
                destinationId = application.value?.id.toString(),
                timestamp = formattedDate
            )

            val newsDocRef = db.collection("news").document(news.id.toString())
            newsDocRef.set(news)
        }
    }

    fun removeApplicant() {
        viewModelScope.launch {
            val docRef = db.collection("posts").document(application.value?.postId!!)
            docRef.update("confirmedApplicants", FieldValue.arrayRemove(user.value?.userId))
        }
    }

    companion object {
        private const val TAG = "APPLICANT_VIEW_MODEL"
    }
}
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
import com.grosianu.jobseeker.models.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ApplyCheckDetailsViewModel : ViewModel() {

    private var _application = MutableLiveData<Application>()
    val application: LiveData<Application> = _application

    private var _resume = MutableLiveData<Resume>()
    val resume: LiveData<Resume> = _resume

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun createApplication(user: User?, postId: String, resumeId: String, message: String) {
        val id: String = UUID.randomUUID().toString()
        val applicantId: String = user?.userId.toString()
        val applicantName: String = user?.displayName.toString()
        val applicantImageUrl: String = user?.imageUri.toString()
        val applicantEmail: String = user?.userEmail.toString()
        val applicantExperience: String = user?.experiencePosition.toString()

        _application.value = Application(
            id,
            applicantId,
            applicantName,
            applicantImageUrl,
            applicantEmail,
            applicantExperience,
            postId,
            resumeId,
            message,
            seen = false,
            confirmed = false,
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
            docRef.get().addOnSuccessListener { document ->
                var post: Post? = null

                if (document != null) {
                    post = document.toObject<Post>()
                }

                if (post != null) {
                    val timestamp = LocalDateTime.now()
                    val formattedDate = "${timestamp.dayOfMonth} ${timestamp.month}, ${timestamp.year}"

                    val news = News(
                        id = UUID.randomUUID().toString(),
                        userId = post.owner,
                        title = "New application!",
                        message = "Someone just applied to this post: ${post.title}.",
                        seen = false,
                        type = "NEW_APPLICATION",
                        destinationId = application.value?.id.toString(),
                        timestamp = formattedDate
                    )

                    val newsDocRef = db.collection("news").document(news.id.toString())
                    newsDocRef.set(news)
                }
            }
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
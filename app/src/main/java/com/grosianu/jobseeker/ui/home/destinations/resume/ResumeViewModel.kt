package com.grosianu.jobseeker.ui.home.destinations.resume

import android.net.Uri
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
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ResumeViewModel : ViewModel() {

    private var _resumes = MutableLiveData<List<Resume>>()
    val resumes: LiveData<List<Resume>> = _resumes

    private var _resume = MutableLiveData<Resume>()
    val resume: LiveData<Resume> = _resume

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private var _hasResume = MutableLiveData(false)
    val hasResume: LiveData<Boolean> = _hasResume

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
                    _hasResume.value = !resumes.isNullOrEmpty()
                }
        }
    }

    fun updateFirestoreData(fileUri: Uri, fileTitle: String) {
        val fileName = UUID.randomUUID().toString()
        val storageRef = storage.getReference("resumes/$fileName")

        storageRef.putFile(fileUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    updateUserFirestoreData(it.toString(), fileTitle)
                }
            }
    }

    private fun updateUserFirestoreData(downloadUri: String, fileTitle: String) {
        val id: String = UUID.randomUUID().toString()
        val url: String = downloadUri
        val owner = auth.currentUser?.uid.toString()
        val applicationTitle = auth.currentUser?.displayName.toString().replace(" ", "_").lowercase()
        val dateCreated = getDate()

        val resume = Resume(
            id = id,
            url = url,
            owner = owner,
            title = fileTitle,
            applicationTitle = applicationTitle,
            image = null,
            dateCreated = dateCreated,
        )

        db.collection("resumes").document(id)
            .set(resume)
            .addOnSuccessListener {
            }
    }

    private fun getDate(): String {
        val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return sdf.format(Date()).toString()
    }

    companion object {
        private const val TAG = "RESUME_VIEW_MODEL"
    }
}
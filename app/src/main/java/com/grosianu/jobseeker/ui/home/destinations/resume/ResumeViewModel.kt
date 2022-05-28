package com.grosianu.jobseeker.ui.home.destinations.resume

import android.graphics.pdf.PdfDocument
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.models.Resume
import kotlinx.coroutines.launch

class ResumeViewModel : ViewModel() {

    private var _resumes = MutableLiveData<List<Resume>>()
    val resumes: LiveData<List<Resume>> = _resumes

    private var _resume = MutableLiveData<Resume>()
    val resume: LiveData<Resume> = _resume

    private val db = FirebaseFirestore.getInstance()
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



    companion object {
        private const val TAG = "RESUME_VIEW_MODEL"
    }
}
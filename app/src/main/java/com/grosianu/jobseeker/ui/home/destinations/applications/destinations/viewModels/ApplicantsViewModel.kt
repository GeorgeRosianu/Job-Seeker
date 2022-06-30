package com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.models.Post
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ApplicantsViewModel : ViewModel() {

    private var _applicants = MutableLiveData<List<Application>>()
    val applicants: LiveData<List<Application>> = _applicants

    private var _applicant = MutableLiveData<Application>()
    val applicant: LiveData<Application> = _applicant

    private val db = FirebaseFirestore.getInstance()

    fun getApplicantList(postId: String) {
        val applicantsTemp = ArrayList<Application>()
        viewModelScope.launch {
            val docRef = db.collection("applications")
            docRef
                .whereEqualTo("postId", postId)
                .whereNotEqualTo("confirmed", true)
                .get()
                .addOnCompleteListener {
                    for (document in it.result) {
                        val applicantTemp = document.toObject<Application>()
                        applicantsTemp.add(applicantTemp)
                    }
                    _applicants.value = applicantsTemp
                }
                .addOnFailureListener {
                    _applicants.value = listOf()
                }
        }
    }

    fun search(p0: String) {
        val search = p0.lowercase(Locale.getDefault())
        val arrayList = ArrayList<Application>()

        applicants.value?.forEach {
            if (it.applicantName?.lowercase(Locale.getDefault())?.contains(search) == true ||
                it.applicantEmail?.lowercase(Locale.getDefault())?.contains(search) == true ||
                it.applicantExperience?.lowercase(Locale.getDefault())?.contains(search) == true
            ) {
                arrayList.add(it)
            }
        }
        _applicants.value = arrayList
    }

    companion object {
        private const val TAG = "APPLICANTS_VIEW_MODEL"
    }
}
package com.grosianu.jobseeker.ui.home.destinations.discover

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.grosianu.jobseeker.models.Application
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class DiscoverViewModel : ViewModel() {

    private var _posts = MutableLiveData<List<Application>>()
    val posts: LiveData<List<Application>> = _posts

    private var _post = MutableLiveData<Application>()
    val post: LiveData<Application> = _post

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    fun getPostList() {
        val postsTmp = ArrayList<Application>()
        viewModelScope.launch {
            val docRef = db.collection("applications")
            docRef.whereNotEqualTo("owner", auth.currentUser?.uid)
                .get()
                .addOnCompleteListener {
                    for (document in it.result) {
                        val postTmp = document.toObject<Application>()
                        postsTmp.add(postTmp)
                    }
                    _posts.value = postsTmp
                }
                .addOnFailureListener {
                    _posts.value = listOf()
                }
        }
    }

    fun search(p0: String) {
        val search = p0.lowercase(Locale.getDefault())
        val arrayList = ArrayList<Application>()

        posts.value?.forEach {
            if (it.title?.lowercase(Locale.getDefault())?.contains(search) == true ||
                it.company?.lowercase(Locale.getDefault())?.contains(search) == true ||
                it.location?.lowercase(Locale.getDefault())?.contains(search) == true ||
                it.industry?.lowercase(Locale.getDefault())?.contains(search) == true ||
                it.level?.lowercase(Locale.getDefault())?.contains(search) == true ||
                it.tags?.contains(search) == true
            ) {
                arrayList.add(it)
            }
        }
        _posts.value = arrayList
    }

    fun filter(options: ArrayList<String>) {
        val arrayList = ArrayList<Application>()

        posts.value?.forEach {
            it.tags?.forEach { tag ->
                if (options.contains(tag)) {
                    arrayList.add(it)
                }
            }
        }
        _posts.value = arrayList
    }

    fun sort(asc: Boolean) {
        val arrayList = when (asc) {
            true -> {
                posts.value?.sortedBy { it.title }
            }
            false -> {
                posts.value?.sortedByDescending { it.title }
            }
        }
        if (arrayList != null) {
            _posts.value = arrayList!!
        }
    }

    fun userAddApplicant(documentId: String) {
        viewModelScope.launch {
            val docRef = db.collection("applications").document(documentId)
            docRef.update("applicants", FieldValue.arrayUnion(auth.currentUser?.uid))
        }
    }

    fun userRemoveApplicant(documentId: String) {
        viewModelScope.launch {
            val docRef = db.collection("applications").document(documentId)
            docRef.update("applicants", FieldValue.arrayRemove(auth.currentUser?.uid))
        }
    }

    companion object {
        private const val TAG = "HOME_VIEW_MODEL"
    }
}
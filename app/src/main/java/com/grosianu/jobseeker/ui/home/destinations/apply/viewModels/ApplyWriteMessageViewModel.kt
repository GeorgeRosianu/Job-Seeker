package com.grosianu.jobseeker.ui.home.destinations.apply.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.models.Resume
import kotlinx.coroutines.launch
import java.util.*

class ApplyWriteMessageViewModel : ViewModel() {

    private var _resume = MutableLiveData<Resume>()
    val resume: LiveData<Resume> = _resume

    private var _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val db = FirebaseFirestore.getInstance()

    companion object {
        private const val TAG = "APPLY_VIEW_MODEL"
    }
}
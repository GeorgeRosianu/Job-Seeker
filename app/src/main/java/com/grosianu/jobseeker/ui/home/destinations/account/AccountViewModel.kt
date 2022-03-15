package com.grosianu.jobseeker.ui.home.destinations.account

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AccountViewModel : ViewModel() {

    private var _auth: FirebaseAuth = FirebaseAuth.getInstance()
    val auth: FirebaseAuth
        get() = _auth

//    private var _currentAccount = MutableLiveData<User>()
//    val currentAccount: LiveData<User> = _currentAccount

    init {

    }
}
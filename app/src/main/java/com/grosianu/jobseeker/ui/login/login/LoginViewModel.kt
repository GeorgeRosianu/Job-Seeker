package com.grosianu.jobseeker.ui.login.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private var _auth: FirebaseAuth = FirebaseAuth.getInstance()
    val auth: FirebaseAuth
        get() = _auth
}
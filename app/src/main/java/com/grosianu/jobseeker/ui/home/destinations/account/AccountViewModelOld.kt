package com.grosianu.jobseeker.ui.home.destinations.account
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.grosianu.jobseeker.models.Application
//import com.grosianu.jobseeker.models.User
//import com.grosianu.jobseeker.services.FirebaseAccountService
//import kotlinx.coroutines.launch
//
//class AccountViewModel : ViewModel() {
//    private val _userAccount = MutableLiveData<User>()
//    val userAccount: LiveData<User> = _userAccount
//    private val _applications = MutableLiveData<List<Application>>()
//    val applications: LiveData<List<Application>> = _applications
//
//    init {
//        viewModelScope.launch {
//            _userAccount.value = FirebaseAccountService.getAccountData()
//            _applications.value = FirebaseAccountService.getApplications()
//        }
//    }
//
//}
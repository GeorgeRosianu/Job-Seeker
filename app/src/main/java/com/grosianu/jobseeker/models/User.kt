package com.grosianu.jobseeker.models

import com.google.firebase.firestore.DocumentSnapshot

data class User(
    val userId: String = "",
    val displayName: String = "",
    val userEmail: String = "",
    var imageUri: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var phoneNumber: String = "",
    var age: Int = 0,
    var residence: String = "",
    var sex: String = "",
    var educationLevel: String = "",
    var educationSpec: String = "",
    var educationCity: String = "",
    var educationInstitution: String = "",
    var educationDate: String = "",
    var experiencePosition: String = "",
    var experienceCompany: String = "",
    var experienceCity: String = "",
    var experienceIndustry: String = "",
    var experienceYears: String = "",
    var experienceDescription: String = "",
    val resumes: ArrayList<String>? = null,
    val favorites: ArrayList<String>? = null,
    ) {

    @JvmName("getFirstName1")
    fun getFirstName() : String = displayName.substringBefore(" ")

    @JvmName("getLastName1")
    fun getLastName() : String = displayName.substringAfterLast(" ")

    fun getAgeAsString() : String = if (age != 0) {
        age.toString()
    } else {
        ""
    }
}
package com.grosianu.jobseeker.models

data class User(
    val userId: String = "",
    val displayName: String = "",
    val userEmail: String = "",
    val imageUri: String = "",
    val resumes: ArrayList<String>? = null,
    val favorites: ArrayList<String>? = null
    ) {
}
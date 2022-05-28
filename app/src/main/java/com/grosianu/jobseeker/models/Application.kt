package com.grosianu.jobseeker.models

data class Application(
    val id: String? = null,
    val applicantId: String? = null,
    val applicantName: String? = null,
    val applicantImageUrl: String? = null,
    val applicantEmail: String? = null,
    val applicantExperience: String? = null,
    val postId: String? = null,
    val resumeId: String? = null,
    val message: String? = null,
    var seen: Boolean = false,
    var confirmed: Boolean = false,
)

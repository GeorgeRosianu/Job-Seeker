package com.grosianu.jobseeker.models

data class Application(
    val id: String? = null,
    val owner: String? = null,
    val title: String? = null,
    val company: String? = null,
    val industry: String? = null,
    val salary: Double? = null,
    val level: String? = null,
    val experience: String? = null,
    val location: String? = null,
    val otherRequirements: String? = null,
    val description: String? = null,
    val tags: ArrayList<String>? = null,
    val image: String? = null,
    val applicants: ArrayList<String>? = null,
)
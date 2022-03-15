package com.grosianu.jobseeker.models

data class Application(
    val owner: String? = null,
    val title: String? = null,
    val company: String? = null,
    val industry: String? = null,
    val salary: Double? = null,
    val location: String? = null,
    val requirements: String? = null,
    val description: String? = null,
) {
}
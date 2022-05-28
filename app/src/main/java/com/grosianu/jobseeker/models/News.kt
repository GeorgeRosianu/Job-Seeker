package com.grosianu.jobseeker.models

data class News(
    val id: String? = null,
    val userId: String? = null,
    val title: String? = null,
    val message: String? = null,
    var seen: Boolean = false,
    val type: String? = null,
    val destinationId: String? = null,
    val timestamp: String? = null,
)
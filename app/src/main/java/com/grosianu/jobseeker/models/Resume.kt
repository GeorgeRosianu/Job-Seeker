package com.grosianu.jobseeker.models

data class Resume(
    val id: String? = null,
    val url: String? = null,
    val owner: String? = null,
    val title: String? = null,
    val applicationTitle: String? = null,
    val image: String? = null,
    val dateCreated: String? = null
)

data class pdfDetails(
    val id: String = "",
    val url: String = "",
    val owner: String = "",
    val title: String = "",
)
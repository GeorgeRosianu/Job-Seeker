package com.grosianu.jobseeker.models

import java.text.NumberFormat

data class Post(
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
    var image: String? = null,
    val imageId: String? = null,
    val applicants: ArrayList<String>? = null,
    val confirmedApplicants: ArrayList<String>? = null,
) {

    fun getFormattedSalary(): String = NumberFormat.getCurrencyInstance().format(salary)

    fun getFormattedTags(): String? = tags?.joinToString()
}
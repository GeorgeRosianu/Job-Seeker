package com.grosianu.jobseeker.ui

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.MyPostsAdapter

/**
 * Updates the data shown in the [RecyclerView]
 */
@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Application>?) {
    val adapter = recyclerView.adapter as MyPostsAdapter
    adapter.submitList(data)
}
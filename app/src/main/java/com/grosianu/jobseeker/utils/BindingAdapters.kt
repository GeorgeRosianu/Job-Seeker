package com.grosianu.jobseeker.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.MyPostsAdapter
import com.grosianu.jobseeker.ui.home.destinations.discover.DiscoverAdapter
import com.grosianu.jobseeker.ui.home.destinations.home.PostsAdapter

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        imgView.load(imgUrl) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("postListData")
fun bindPostsRecyclerView(recyclerView: RecyclerView, data: List<Application>?) {
    val adapter = recyclerView.adapter as PostsAdapter
    adapter.submitList(data)
}

@BindingAdapter("postListData2")
fun bindPostsRecyclerView2(recyclerView: RecyclerView, data: List<Application>?) {
    val adapter = recyclerView.adapter as MyPostsAdapter
    adapter.submitList(data)
}

@BindingAdapter("discoverListData")
fun bindDiscoverRecyclerView(recyclerView: RecyclerView, data: List<Application>?) {
    val adapter = recyclerView.adapter as DiscoverAdapter
    adapter.submitList(data)
}

//@BindingAdapter("applicationListData")
//fun bindApplicationsRecyclerView(recyclerView: RecyclerView, data: List<Application>?) {
//    val adapter = recyclerView.adapter as PostsAdapter
//    adapter.submitList(data)
//}
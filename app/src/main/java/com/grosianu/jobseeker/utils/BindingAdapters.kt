package com.grosianu.jobseeker.utils

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.models.News
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.models.Resume
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.adapters.ApplicantsAdapter
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.adapters.MyApplicationsAdapter
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.adapters.MyPostsAdapter
import com.grosianu.jobseeker.ui.home.destinations.apply.adapters.ApplySelectResumeAdapter
import com.grosianu.jobseeker.ui.home.destinations.discover.DiscoverAdapter
import com.grosianu.jobseeker.ui.home.destinations.home.adapters.ApplicationsAdapter
import com.grosianu.jobseeker.ui.home.destinations.home.adapters.FavoritesAdapter
import com.grosianu.jobseeker.ui.home.destinations.home.adapters.PostsAdapter
import com.grosianu.jobseeker.ui.home.destinations.notifications.NotificationsAdapter
import com.grosianu.jobseeker.ui.home.destinations.resume.ResumeAdapter

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        imgView.load(imgUrl) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("ImageButtonUrl")
fun bindImageButton(imgButton: ImageButton, imgUrl: String?) {
    imgUrl?.let {
        imgButton.load(imgUrl) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("pdfPreview")
fun bindPdfPreview(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        imgView.load(imgUrl) {
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("isVisible")
fun changeVisibility(fab: ExtendedFloatingActionButton, data: Boolean) {
    if (data) {
        fab.visibility = View.VISIBLE
    } else {
        fab.visibility = View.GONE
    }
}

@BindingAdapter("postListData")
fun bindPostsRecyclerView(recyclerView: RecyclerView, data: List<Post>?) {
    val adapter = recyclerView.adapter as PostsAdapter
    adapter.submitList(data)
}

@BindingAdapter("postListData2")
fun bindPostsRecyclerView2(recyclerView: RecyclerView, data: List<Post>?) {
    val adapter = recyclerView.adapter as MyPostsAdapter
    adapter.submitList(data)
}

@BindingAdapter("favoritesListData")
fun bindFavoritesHomeRecyclerView(recyclerView: RecyclerView, data: List<Post>?) {
    val adapter = recyclerView.adapter as FavoritesAdapter
    adapter.submitList(data)
}

@BindingAdapter("discoverListData")
fun bindDiscoverRecyclerView(recyclerView: RecyclerView, data: List<Post>?) {
    val adapter = recyclerView.adapter as DiscoverAdapter
    adapter.submitList(data)
}

@BindingAdapter("applicationListData")
fun bindApplicationsRecyclerView(recyclerView: RecyclerView, data: List<Post>?) {
    val adapter = recyclerView.adapter as MyApplicationsAdapter
    adapter.submitList(data)
}

@BindingAdapter("applicationListData2")
fun bindApplicationsRecycleView2(recyclerView: RecyclerView, data: List<Post>?) {
    val adapter = recyclerView.adapter as ApplicationsAdapter
    adapter.submitList(data)
}

@BindingAdapter("resumeListData")
fun bindResumeRecyclerView(recyclerView: RecyclerView, data: List<Resume>?) {
    val adapter = recyclerView.adapter as ResumeAdapter
    adapter.submitList(data)
}

@BindingAdapter("applyResumeListData")
fun bindApplyResumeRecyclerView(recyclerView: RecyclerView, data: List<Resume>?) {
    val adapter = recyclerView.adapter as ApplySelectResumeAdapter
    adapter.submitList(data)
}

@BindingAdapter("applicantListData")
fun bindApplicantsRecyclerView(recyclerView: RecyclerView, data: List<Application>?) {
    val adapter = recyclerView.adapter as ApplicantsAdapter
    adapter.submitList(data)
}

@BindingAdapter("newsListData")
fun bindNewsRecyclerView(recyclerView: RecyclerView, data: List<News>?) {
    val adapter = recyclerView.adapter as NotificationsAdapter
    adapter.submitList(data)
}
package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentMyPostsBinding
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.adapters.MyPostsAdapter
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels.MyPostsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class MyPostsFragment : Fragment(), MyPostsAdapter.MyPostsAdapterListener {

    private var _binding: FragmentMyPostsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPostsViewModel by viewModels()
    private val myPostsAdapter = MyPostsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization(view)
    }

    private fun initialization(view: View) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        updateRecycleView()
        setupViews()
    }

    private fun setupViews() {
        binding.lifecycleOwner = this
        binding.swipeView.setOnRefreshListener {
            refreshPostList()
        }

        binding.fabAddOffer.apply {
//            setShowMotionSpecResource(R.animator.fab_show)
//            setShowMotionSpecResource(R.animator.fab_hide)
            setOnClickListener {
                navigateToCreate()
            }
        }
    }

    private fun setupViewModel() {
        viewModel.getPostList()
        binding.viewModel = viewModel
    }

    private fun updateRecycleView() {
        viewModel.getPostList()
        binding.viewModel = viewModel
        binding.recyclerView.adapter = myPostsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPostClicked(cardView: View, post: Post) {
        val postCardDetailTransitionName = getString(R.string.post_card_detail_transition_name)
        val extras = FragmentNavigatorExtras(cardView to postCardDetailTransitionName)
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        val directions = MyPostsFragmentDirections.actionMyPostsFragmentToEditPostFragment(post.id.toString())
        findNavController().navigate(directions, extras)
    }

    override fun onPostLongPressed(post: Post): Boolean {
        TODO("Not yet implemented")
    }

    private fun refreshPostList() {
        binding.swipeView.isRefreshing = false
        updateRecycleView()
    }

    private fun navigateToCreate() {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        val directions = MyPostsFragmentDirections.actionMyPostsFragmentToCreateFragment()
        findNavController().navigate(directions)
    }
}
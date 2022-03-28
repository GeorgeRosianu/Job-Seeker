package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

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
import com.grosianu.jobseeker.models.Application

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

        // Postpone enter transitions to allow shared element transitions to run.
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        viewModel.getPostList()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.recyclerView.adapter = myPostsAdapter

        binding.swipeView.setOnRefreshListener {
            updatePostList()
        }

        binding.fabAddOffer.apply {
            setShowMotionSpecResource(R.animator.fab_show)
            setShowMotionSpecResource(R.animator.fab_hide)
            setOnClickListener {
                navigateToCreate()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPostClicked(cardView: View, application: Application) {
        val postCardDetailTransitionName = getString(R.string.post_card_detail_transition_name)
        val extras = FragmentNavigatorExtras(cardView to postCardDetailTransitionName)
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        val directions = MyPostsFragmentDirections.actionMyPostsFragmentToEditPostFragment(application.id.toString())
        findNavController().navigate(directions, extras)
    }

    override fun onPostLongPressed(application: Application): Boolean {
        TODO("Not yet implemented")
    }

    private fun updatePostList() {
        binding.swipeView.isRefreshing = false

        viewModel.getPostList()
        binding.viewModel = viewModel
        binding.recyclerView.adapter = myPostsAdapter
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
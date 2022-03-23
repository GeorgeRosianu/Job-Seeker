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
import com.grosianu.jobseeker.ui.home.destinations.applications.ApplicationsFragmentDirections

class MyPostsFragment : Fragment(), MyPostsAdapter.MyPostsAdapterListener {

    private var _binding: FragmentMyPostsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPostsViewModel by viewModels()
    private val myPostsAdapter = MyPostsAdapter(this)

    companion object {
        fun newInstance() = MyPostsFragment()
    }

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
        binding.recyclerView.apply {
            adapter = myPostsAdapter
        }
        binding.recyclerView.adapter = myPostsAdapter

        binding.swipeView.setOnRefreshListener {
            updatePostList()
        }

        binding.fabAddOffer.apply {
//            setShowMotionSpecResource(R.animator.fab_show)
//            setShowMotionSpecResource(R.animator.fab_hide)
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
        val action = ApplicationsFragmentDirections
            .actionApplicationsFragmentToEditPostFragment(application.id.toString())
        findNavController().navigate(action)
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
        val directions = ApplicationsFragmentDirections.actionApplicationsFragmentToCreateFragment()
        findNavController().navigate(directions)
    }
}
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
import com.grosianu.jobseeker.databinding.FragmentMyApplicationsBinding
import com.grosianu.jobseeker.models.Application

class MyApplicationsFragment: Fragment(), MyApplicationsAdapter.MyApplicationsAdapterListener {

    private var _binding: FragmentMyApplicationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyApplicationsViewModel by viewModels()
    private val myApplicationsAdapter = MyApplicationsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyApplicationsBinding.inflate(inflater, container, false)
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

    private fun setupViewModel() {
        viewModel.getPostList()
        binding.viewModel = viewModel
    }

    private fun setupViews() {
        binding.lifecycleOwner = this
        binding.swipeView.setOnRefreshListener {
            refreshApplicationList()
        }
    }

    private fun updateRecycleView() {
        viewModel.getPostList()
        binding.viewModel = viewModel
        binding.recyclerView.adapter = myApplicationsAdapter
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
        val directions = MyApplicationsFragmentDirections.actionGlobalApplicationFragment(application.id.toString())
        findNavController().navigate(directions, extras)
    }

    override fun onPostLongPressed(application: Application): Boolean {
        TODO("Not yet implemented")
    }

    private fun refreshApplicationList() {
        binding.swipeView.isRefreshing = false
        updateRecycleView()
    }

    private fun navigateToApply() {
        // TODO Implement
    }
}
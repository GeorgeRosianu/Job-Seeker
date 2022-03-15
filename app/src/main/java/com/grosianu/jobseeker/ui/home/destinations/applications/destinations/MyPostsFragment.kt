package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentMyPostsBinding
import com.grosianu.jobseeker.ui.home.destinations.applications.ApplicationsFragment
import com.grosianu.jobseeker.ui.home.destinations.applications.ApplicationsFragmentDirections
import com.grosianu.jobseeker.ui.login.StartupActivity
import com.skydoves.transformationlayout.addTransformation
import com.skydoves.transformationlayout.onTransformationStartContainer

class MyPostsFragment: Fragment() {

    private var _binding: FragmentMyPostsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyPostsViewModel by viewModels()

    companion object {
        fun newInstance() = MyPostsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyPostsBinding.inflate(inflater, container, false)

        viewModel.getPostList()

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.recyclerView.adapter = MyPostsAdapter()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    private fun navigateToCreate() {
        this.apply {
            exitTransition = MaterialElevationScale(false).apply {
                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            }
        }
        val directions = ApplicationsFragmentDirections.actionApplicationsFragmentToCreateFragment()
        findNavController().navigate(directions)
    }
}
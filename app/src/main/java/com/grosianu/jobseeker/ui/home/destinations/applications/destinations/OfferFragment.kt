package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentOfferBinding
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels.OfferViewModel
import com.grosianu.jobseeker.utils.themeColor

class OfferFragment : Fragment() {

    private var _binding: FragmentOfferBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OfferViewModel by viewModels()
    private val args: OfferFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOfferBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSharedTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization()
    }

    private fun initialization() {
        setupViewModel()
        setupViews()
    }

    private fun setupViews() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.navigationIcon.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.applyBtn.setOnClickListener {
            val start = "post"
            val directions = OfferFragmentDirections.actionGlobalApplySelectResumeFragment(args.postId, start)
            findNavController().navigate(directions)
        }
    }

    private fun setupViewModel() {
        viewModel.getPost(args.postId)
        binding.viewModel = viewModel
    }

    private fun setupSharedTransition() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment_home
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }
}
package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentOfferBinding
import com.grosianu.jobseeker.ui.home.HomeActivityViewModel
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels.OfferViewModel
import com.grosianu.jobseeker.utils.themeColor

class OfferFragment : Fragment() {

    private var binding: FragmentOfferBinding? = null

    private val viewModel: OfferViewModel by viewModels()
    private val sharedViewModel: HomeActivityViewModel by activityViewModels()
    private val args: OfferFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentOfferBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSharedTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupViews()
    }

    private fun setupViews() {
        binding?.lifecycleOwner = viewLifecycleOwner
        binding?.navigationIcon?.setOnClickListener {
            findNavController().navigateUp()
        }
        binding?.applyBtn?.setOnClickListener {
            val start = "post"
            val isUserSetUp = sharedViewModel.isUserSetUp.value ?: false
            val hasResumes = sharedViewModel.hasResumes.value ?: false
            val directions = OfferFragmentDirections.actionGlobalApplySelectResumeFragment(args.postId, start, isUserSetUp)

            if (hasResumes || isUserSetUp) {
                findNavController().navigate(directions)
            } else {
                alertMissingInfo()
            }
        }
    }

    private fun setupViewModel() {
        viewModel.getPost(args.postId)
        binding?.viewModel = viewModel
    }

    private fun setupSharedTransition() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment_home
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }

    private fun alertMissingInfo() {
        AlertDialog.Builder(requireContext())
            .setTitle("Your profile is not set up!")
            .setMessage("Please complete your profile or upload a CV to apply.")
            .setPositiveButton("Ok") { dialog, _ -> dialog.cancel() }
            .show()
    }
}
package com.grosianu.jobseeker.ui.home.destinations.apply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.grosianu.jobseeker.databinding.FragmentApplyChooseResumeBinding
import com.grosianu.jobseeker.models.Resume
import com.grosianu.jobseeker.ui.home.HomeActivity
import com.grosianu.jobseeker.ui.home.HomeActivityViewModel
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.OfferFragment
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.OfferFragmentDirections
import com.grosianu.jobseeker.ui.home.destinations.apply.adapters.ApplySelectResumeAdapter
import com.grosianu.jobseeker.ui.home.destinations.apply.viewModels.ApplySelectResumeViewModel
import com.grosianu.jobseeker.ui.home.destinations.discover.DiscoverFragmentDirections

class ApplySelectResumeFragment : Fragment(), ApplySelectResumeAdapter.ApplySelectResumeAdapterListener {

    private var binding: FragmentApplyChooseResumeBinding? = null
    private val viewModel: ApplySelectResumeViewModel by viewModels()
    private val sharedViewModel: HomeActivityViewModel by activityViewModels()
    private val adapter = ApplySelectResumeAdapter(this)
    private val args: ApplySelectResumeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentApplyChooseResumeBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateRecyclerView()
        setupViews()
    }

    private fun setupViews() {
        sharedViewModel.getResumeList()
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner

            cancelIcon.setOnClickListener {
                navigateBack()
            }
            navigationIcon.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }

    private fun updateRecyclerView() {
        binding?.apply {
            viewModel = sharedViewModel
            recyclerView.adapter = adapter
        }
    }

    override fun onResumeClicked(resume: Resume) {
        val directions = ApplySelectResumeFragmentDirections.actionApplySelectResumeFragmentToApplyWriteMessageFragment(resume.id.toString(), args.postId, args.start)
        findNavController().navigate(directions)
    }

    private fun navigateBack() {
        val directions = if (args.start == "post") {
            ApplySelectResumeFragmentDirections.actionApplySelectResumeFragmentToApplicationFragment(
                args.postId
            )
        } else {
            ApplySelectResumeFragmentDirections.actionApplySelectResumeFragmentToDiscoverFragment()
        }
        findNavController().navigate(directions)
    }
}
package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.grosianu.jobseeker.databinding.FragmentApplicantsBinding
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.ui.home.HomeActivityViewModel
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.adapters.ApplicantsAdapter
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels.ApplicantsViewModel

class ApplicantsFragment : Fragment(), ApplicantsAdapter.ApplicantsAdapterListener {

    private var binding: FragmentApplicantsBinding? = null

    private val viewModel: ApplicantsViewModel by viewModels()
    private val sharedViewModel: HomeActivityViewModel by activityViewModels()
    private val args: ApplicantsFragmentArgs by navArgs()
    private val applicantsAdapter = ApplicantsAdapter(this)

    private var asc: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentApplicantsBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        updateRecycleView()
        setupViews()
    }

    private fun setupViews() {
        binding?.lifecycleOwner = viewLifecycleOwner
        binding?.swipeView?.setOnRefreshListener {
            refreshApplicantList()
        }
        viewModel.hasApplicants.observe(viewLifecycleOwner) {
            if (it) {
                binding?.placeholderTextView?.visibility = View.INVISIBLE
            } else {
                binding?.placeholderTextView?.visibility = View.VISIBLE
            }
        }
    }

    private fun setupViewModel() {
        viewModel.getApplicantList(args.postId)
        binding?.viewModel = viewModel
    }

    private fun updateRecycleView() {
        viewModel.getApplicantList(args.postId)
        binding?.viewModel = viewModel
        binding?.recyclerView?.adapter = applicantsAdapter
    }

    private fun refreshApplicantList() {
        binding?.swipeView?.isRefreshing = false
        updateRecycleView()
    }

    override fun onApplicantClicked(cardView: View, application: Application) {
        val directions = ApplicantsFragmentDirections.actionApplicantsFragmentToApplicantFragment(application.applicantId.toString(), application.id.toString())
        findNavController().navigate(directions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.grosianu.jobseeker.databinding.FragmentApplicantsBinding
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.adapters.ApplicantsAdapter
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels.ApplicantsViewModel

class ApplicantsFragment : Fragment(), ApplicantsAdapter.ApplicantsAdapterListener {

    private var _binding: FragmentApplicantsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ApplicantsViewModel by viewModels()
    private val args: ApplicantsFragmentArgs by navArgs()
    private val applicantsAdapter = ApplicantsAdapter(this)

    private var asc: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApplicantsBinding.inflate(inflater, container, false)
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
            refreshApplicantList()
        }
    }

    private fun setupViewModel() {
        viewModel.getApplicantList(args.postId)
        binding.viewModel = viewModel
    }

    private fun updateRecycleView() {
        viewModel.getApplicantList(args.postId)
        binding.viewModel = viewModel
        binding.recyclerView.adapter = applicantsAdapter
    }

    private fun refreshApplicantList() {
        binding.swipeView.isRefreshing = false
        updateRecycleView()
    }

    override fun onApplicantClicked(cardView: View, application: Application) {
        Toast.makeText(requireContext(), "Click.", Toast.LENGTH_SHORT).show()
        TODO("Not yet implemented")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
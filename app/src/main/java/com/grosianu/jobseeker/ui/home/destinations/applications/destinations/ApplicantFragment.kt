package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.grosianu.jobseeker.databinding.FragmentApplicantBinding
import com.grosianu.jobseeker.ui.home.HomeActivityViewModel
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels.ApplicantViewModel
import com.grosianu.jobseeker.ui.home.destinations.resume.ResumeFragmentDirections

class ApplicantFragment : Fragment() {

    private var binding: FragmentApplicantBinding? = null

    private val viewModel: ApplicantViewModel by viewModels()
    private val sharedViewModel: HomeActivityViewModel by activityViewModels()
    private val args: ApplicantFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentApplicantBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        setupViewModel()

        binding?.lifecycleOwner = viewLifecycleOwner

        viewModel.hasStudies.observe(viewLifecycleOwner) {
            if (!it) {
                binding?.studiesLayout?.visibility = View.GONE
            } else {
                binding?.studiesLayout?.visibility = View.VISIBLE
            }
        }
        viewModel.hasExperience.observe(viewLifecycleOwner) {
            if (!it) {
                binding?.experienceLayout?.visibility = View.GONE
            } else {
                binding?.experienceLayout?.visibility = View.VISIBLE
            }
        }

        binding?.fab?.setOnClickListener {
            alertConfirmation()
        }

        binding?.resumeIcon?.setOnClickListener {
            val directions = ResumeFragmentDirections.actionGlobalPdfViewFragment(viewModel.resume.value?.url.toString())
            findNavController().navigate(directions)
        }

        binding?.navigationIcon?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupViewModel() {
        viewModel.getUserData(args.userId)
        viewModel.getUserApplication(args.applicationId)
        binding?.viewModel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun alertConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to proceed?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.confirmApplicant()
                Toast.makeText(requireContext(), "Applicant added", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            .show()
    }
}
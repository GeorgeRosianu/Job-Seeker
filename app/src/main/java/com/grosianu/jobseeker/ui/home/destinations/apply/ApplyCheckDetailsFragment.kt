package com.grosianu.jobseeker.ui.home.destinations.apply

import android.content.DialogInterface
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentApplyCheckDetailsBinding
import com.grosianu.jobseeker.ui.home.HomeActivityViewModel
import com.grosianu.jobseeker.ui.home.destinations.apply.viewModels.ApplyCheckDetailsViewModel

class ApplyCheckDetailsFragment : Fragment() {

    private var binding: FragmentApplyCheckDetailsBinding? = null

    private val viewModel: ApplyCheckDetailsViewModel by viewModels()
    private val sharedViewModel: HomeActivityViewModel by activityViewModels()
    private val args: ApplyCheckDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentApplyCheckDetailsBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        val user = sharedViewModel.currentAccount.value
        viewModel.createApplication(user, args.postId, args.resumeId, args.message)

        binding?.apply {
            message.text = args.message

            cancelIcon.setOnClickListener {
                navigateBack()
            }
            navigationIcon.setOnClickListener {
                findNavController().navigateUp()
            }
            applyBtn.setOnClickListener {
                alertConfirmation()
            }
        }
    }

    private fun navigateBack() {
        val directions = when (args.start) {
            "posts" -> ApplyCheckDetailsFragmentDirections
                .actionApplyCheckDetailsFragmentToApplicationFragment(args.postId)
            "discover" ->  ApplyCheckDetailsFragmentDirections
                .actionApplyCheckDetailsFragmentToDiscoverFragment()
            else -> ApplyCheckDetailsFragmentDirections.actionGlobalHomeFragment()
        }
        findNavController().navigate(directions)
    }

    private fun alertConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to apply?")
            .setPositiveButton("Yes") { _, _ ->
                val application = viewModel.application.value!!
                viewModel.uploadApplication(requireContext(), application)
                viewModel.userAddApplicant(args.postId)
                Toast.makeText(requireContext(), "Application complete",Toast.LENGTH_SHORT).show()
                navigateBack()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            .show()
    }
}
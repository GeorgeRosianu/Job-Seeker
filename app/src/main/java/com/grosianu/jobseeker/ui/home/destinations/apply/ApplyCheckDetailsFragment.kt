package com.grosianu.jobseeker.ui.home.destinations.apply

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.dialog.MaterialDialogs
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentApplyCheckDetailsBinding
import com.grosianu.jobseeker.ui.home.destinations.apply.viewModels.ApplyCheckDetailsViewModel

class ApplyCheckDetailsFragment : Fragment() {

    private var _binding: FragmentApplyCheckDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ApplyCheckDetailsViewModel by viewModels()
    private val args: ApplyCheckDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApplyCheckDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization()
    }

    private fun initialization() {
        viewModel.createApplication(args.postId, args.resumeId, args.message)
        setupViews()
    }

    private fun setupViews() {
        binding.viewModel = viewModel
        binding.cancelIcon.setOnClickListener {
            navigateBack()
        }
        binding.navigationIcon.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.applyBtn.setOnClickListener {
            alertConfirmation()
        }
    }

    private fun navigateBack() {
        val directions = if (args.start == "posts") {
            ApplyCheckDetailsFragmentDirections.actionApplyCheckDetailsFragmentToApplicationFragment(
                args.postId
            )
        } else {
            ApplyCheckDetailsFragmentDirections.actionApplyCheckDetailsFragmentToDiscoverFragment()
        }
        findNavController().navigate(directions)
    }

    private fun alertConfirmation() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Apply Confirmation")
            .setMessage("Are you sure you want to apply?")
            .setNegativeButton("No") { dialog, which ->
                dialog.cancel()
            }
            .setPositiveButton("Yes") { dialog, which ->
                val application = viewModel.application.value!!
                viewModel.uploadApplication(requireContext(), application)
                viewModel.userAddApplicant(args.postId)
                Toast.makeText(requireContext(), "Application complete!",Toast.LENGTH_SHORT).show()
                navigateBack()
            }
            .show()
//        val positiveButtonClick = { _: DialogInterface, _: Int ->
//
//        }
//
//        val negativeButtonClick = { dialog: DialogInterface, _: Int ->
//            dialog.cancel()
//        }
//
//        val builder = AlertDialog.Builder(requireContext())
//        builder.setTitle("Apply Confirmation")
//        builder.setMessage("Are you sure you want to apply?")
//        builder.setPositiveButton("Yes", DialogInterface.OnClickListener(function = positiveButtonClick))
//        builder.setNegativeButton("No", negativeButtonClick)
//        builder.show()
    }
}
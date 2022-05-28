package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import android.content.DialogInterface
import android.graphics.Color
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
import com.google.android.material.transition.MaterialContainerTransform
import com.google.firebase.auth.FirebaseAuth
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentApplicationBinding
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels.ApplicationViewModel
import com.grosianu.jobseeker.utils.themeColor

class ApplicationFragment : Fragment() {

    private var _binding: FragmentApplicationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ApplicationViewModel by viewModels()
    private val args: ApplicationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApplicationBinding.inflate(inflater, container, false)
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

        viewModel.hasBeenConfirmed.observe(viewLifecycleOwner) {
            if (it) {
                binding.deleteBtn.visibility = View.GONE
            } else {
                binding.deleteBtn.visibility = View.VISIBLE
            }
        }
        binding.navigationIcon.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.deleteBtn.setOnClickListener {
            alertConfirmation()
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

    private fun navigateBack() {
        findNavController().navigateUp()
    }

    private fun alertConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Confirmation")
            .setMessage("Are you sure you want to remove this application?")
            .setPositiveButton("Yes") { _, _ ->
                viewModel.userRemoveApplicant(args.postId)
                Toast.makeText(requireContext(), "Application deleted", Toast.LENGTH_SHORT).show()
                navigateBack()
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
            .show()
    }
}
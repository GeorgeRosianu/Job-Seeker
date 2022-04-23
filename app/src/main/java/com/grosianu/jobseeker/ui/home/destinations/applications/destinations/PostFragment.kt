package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentPostBinding
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels.PostViewModel
import com.grosianu.jobseeker.utils.themeColor

class PostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PostViewModel by viewModels()
    private val args: PostFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSharedTransition()
    }

    override fun onResume() {
        super.onResume()

        setupViewModel()
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
        binding.applicantsIcon.setOnClickListener {
            val directions = PostFragmentDirections.actionEditPostFragmentToApplicantsFragment(args.postId)
            findNavController().navigate(directions)
        }
        binding.editIcon.setOnClickListener {
            val directions =
                PostFragmentDirections.actionGlobalEditApplicationFragment(args.postId)
            findNavController().navigate(directions)
        }
        binding.deleteBtn.setOnClickListener {
            showAlertDialog()
        }
    }

    private fun setupViewModel() {
        viewModel.getPost(args.postId)
        binding.viewModel = viewModel
    }

    private fun showAlertDialog() {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Delete")
        alertDialog.setMessage("Are you sure you want to delete this post?")
        alertDialog.setPositiveButton(
            "Yes"
        ) { _, _ ->
            viewModel.deletePost(args.postId, requireContext())
            findNavController().navigateUp()
        }
        alertDialog.setNegativeButton(
            "No"
        ) { _, _ ->

        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(true)
        alert.show()
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
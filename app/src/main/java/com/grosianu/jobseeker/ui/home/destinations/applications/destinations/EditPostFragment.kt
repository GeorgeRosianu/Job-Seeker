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
import com.grosianu.jobseeker.databinding.FragmentPostBinding
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.utils.themeColor

class EditPostFragment : Fragment() {

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditPostViewModel by viewModels()

    private val args: EditPostFragmentArgs by navArgs()
    //private val postId = args.postId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPost(args.postId)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.navigationIcon.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
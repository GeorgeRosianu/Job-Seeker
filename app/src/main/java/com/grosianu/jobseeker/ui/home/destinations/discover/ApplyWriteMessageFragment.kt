package com.grosianu.jobseeker.ui.home.destinations.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.grosianu.jobseeker.databinding.FragmentApplyWriteMessageBinding

class ApplyWriteMessageFragment : Fragment() {

    private var _binding: FragmentApplyWriteMessageBinding? = null
    private val binding get() = _binding!!

//    private val viewModel: ApplyWriteMessageViewModel by viewModels()
    private val args: ApplyWriteMessageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApplyWriteMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization()
    }

    private fun initialization() {
        setupViews()
    }

    private fun setupViews() {
        binding.cancelIcon.setOnClickListener {
            val directions = if (args.start == "application") {
                ApplyWriteMessageFragmentDirections.actionApplyWriteMessageFragmentToApplicationFragment(
                    args.postId
                )
            } else {
                ApplyWriteMessageFragmentDirections.actionApplyWriteMessageFragmentToDiscoverFragment()
            }
            findNavController().navigate(directions)
        }
        binding.navigationIcon.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
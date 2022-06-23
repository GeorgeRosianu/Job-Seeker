package com.grosianu.jobseeker.ui.home.destinations.apply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.grosianu.jobseeker.databinding.FragmentApplyWriteMessageBinding
import com.grosianu.jobseeker.ui.home.HomeActivityViewModel
import com.grosianu.jobseeker.ui.home.destinations.apply.viewModels.ApplyWriteMessageViewModel

class ApplyWriteMessageFragment : Fragment() {

    private var binding: FragmentApplyWriteMessageBinding? = null

    private val sharedViewModel: HomeActivityViewModel by activityViewModels()
    private val args: ApplyWriteMessageFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentApplyWriteMessageBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        binding?.apply {
            cancelIcon.setOnClickListener {
                navigateBack()
            }
            navigationIcon.setOnClickListener {
                findNavController().navigateUp()
            }
            nextBtn.setOnClickListener {
                val directions = ApplyWriteMessageFragmentDirections.actionApplyWriteMessageFragmentToApplyCheckDetailsFragment(args.postId, args.resumeId, messageEdit.text.toString(), args.start)
                findNavController().navigate(directions)
            }
        }
    }

    private fun navigateBack() {
        val directions = if (args.start == "posts") {
            ApplyWriteMessageFragmentDirections.actionApplyWriteMessageFragmentToApplicationFragment(
                args.postId
            )
        } else {
            ApplyWriteMessageFragmentDirections.actionApplyWriteMessageFragmentToDiscoverFragment()
        }
        findNavController().navigate(directions)
    }
}
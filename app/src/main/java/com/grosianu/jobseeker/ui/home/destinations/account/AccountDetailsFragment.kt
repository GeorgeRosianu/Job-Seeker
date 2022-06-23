package com.grosianu.jobseeker.ui.home.destinations.account

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialContainerTransform
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentAccountDetailsBinding
import com.grosianu.jobseeker.ui.home.HomeActivityViewModel
import com.grosianu.jobseeker.ui.home.destinations.account.viewModels.AccountDetailsViewModel
import com.grosianu.jobseeker.utils.themeColor

class AccountDetailsFragment : Fragment() {

    private var binding: FragmentAccountDetailsBinding? = null
    private val sharedViewModel: HomeActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentAccountDetailsBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner

            editIcon.setOnClickListener {
                val directions = AccountDetailsFragmentDirections.actionAccountDetailsFragmentToAccountAddDetailsFragment()
                findNavController().navigate(directions)
            }

            navigationIcon.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
}
package com.grosianu.jobseeker.ui.home.destinations.account

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialContainerTransform
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentAccountDetailsBinding
import com.grosianu.jobseeker.ui.home.destinations.account.viewModels.AccountDetailsViewModel
import com.grosianu.jobseeker.utils.themeColor

class AccountDetailsFragment : Fragment() {

    private var _binding: FragmentAccountDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AccountDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {

        viewModel.getUserData()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.editIcon.setOnClickListener {
            val directions = AccountDetailsFragmentDirections.actionAccountDetailsFragmentToAccountAddDetailsFragment()
            findNavController().navigate(directions)
        }
        binding.navigationIcon.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}
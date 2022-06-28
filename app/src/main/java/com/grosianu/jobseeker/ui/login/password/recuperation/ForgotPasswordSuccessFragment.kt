package com.grosianu.jobseeker.ui.login.password.recuperation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.grosianu.jobseeker.databinding.FragmentForgotPasswordSuccessBinding

class ForgotPasswordSuccessFragment : Fragment() {

    private var binding: FragmentForgotPasswordSuccessBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentForgotPasswordSuccessBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.loginBtn?.setOnClickListener {
            val action =
                ForgotPasswordSuccessFragmentDirections.actionForgotPasswordSuccessFragmentToLoginFragment()
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
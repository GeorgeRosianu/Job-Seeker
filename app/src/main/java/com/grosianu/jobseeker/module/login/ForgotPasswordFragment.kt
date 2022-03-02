package com.grosianu.jobseeker.module.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.grosianu.jobseeker.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.sendBtn.setOnClickListener {
            val email: String = binding.resetPasswordInputEdit.text.toString()

            val action = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToForgotPasswordSuccessFragment()
            this.findNavController().navigate(action)
        }
        binding.loginBtn.setOnClickListener {
            val action = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment()
            this.findNavController().navigate(action)
        }
    }
}
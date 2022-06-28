package com.grosianu.jobseeker.ui.login.password.recuperation

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.grosianu.jobseeker.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : Fragment() {

    private var binding: FragmentForgotPasswordBinding? = null

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        isEmailValid()

        binding?.sendBtn?.setOnClickListener {
            val email: String = binding?.resetPasswordInputEdit?.text.toString()

            if (binding?.resetPasswordInput?.isErrorEnabled == false) {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(requireActivity()) {
                        if (it.isSuccessful) {
                            val action =
                                ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToForgotPasswordSuccessFragment()
                            this.findNavController().navigate(action)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Unable to send the reset email!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        binding?.loginBtn?.setOnClickListener {
            val action =
                ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun isEmailValid() {
        binding?.resetPasswordInputEdit?.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    binding?.resetPasswordInput?.isErrorEnabled = true
                    binding?.resetPasswordInput?.error = "The email address is invalid"
                } else {
                    binding?.resetPasswordInput?.isErrorEnabled = false
                    binding?.resetPasswordInput?.error = null
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
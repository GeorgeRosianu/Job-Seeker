package com.grosianu.jobseeker.module.login

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.sendBtn.setOnClickListener {
            val email: String = binding.resetPasswordInputEdit.text.toString()
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(
                    requireContext(),
                    "Please fill the required fields",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(requireActivity(), OnCompleteListener {
                        if (it.isSuccessful) {
                            val action = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToForgotPasswordSuccessFragment()
                            this.findNavController().navigate(action)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Unable to send the reset email!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }
        binding.loginBtn.setOnClickListener {
            val action =
                ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment()
            this.findNavController().navigate(action)
        }
    }
}
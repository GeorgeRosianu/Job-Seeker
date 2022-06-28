package com.grosianu.jobseeker.ui.login.register

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
import com.google.firebase.firestore.FirebaseFirestore
import com.grosianu.jobseeker.databinding.FragmentRegisterBinding

private const val PASSWORD_REQUIRED_LENGTH = 8

class RegisterFragment : Fragment() {

    private var binding: FragmentRegisterBinding? = null
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        isEmailValid()
        isPasswordValid()

        binding?.createAccountBtn?.setOnClickListener {
            val email = binding?.createEmailInputEdit?.text.toString()
            val password = binding?.createPasswordInputEdit?.text.toString()

            if (isRegisterFormValid()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Account successfully created",
                                Toast.LENGTH_LONG
                            ).show()
                            val action =
                                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                            this.findNavController().navigate(action)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Sign up unsuccessful",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
            }
        }

        binding?.loginBtn?.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun isRegisterFormValid(): Boolean {
        if (binding?.createEmailInput?.isErrorEnabled == true ||
            binding?.createPasswordInput?.isErrorEnabled == true) {
            return false
        }
        return true
    }

    private fun isEmailValid() {
        binding?.createEmailInputEdit?.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (!Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    binding?.createEmailInput?.isErrorEnabled = true
                    binding?.createEmailInput?.error = "The email address is invalid"
                } else {
                    binding?.createEmailInput?.isErrorEnabled = false
                    binding?.createEmailInput?.error = null
                }
            }
        }
    }

    private fun isPasswordValid() {
        binding?.createPasswordInputEdit?.doOnTextChanged { text, _, _, _ ->
            when {
                text.isNullOrEmpty() -> {
                    binding?.createPasswordInput?.isErrorEnabled = true
                    binding?.createPasswordInput?.error = "Password field cannot be empty"
                }
                text.length < PASSWORD_REQUIRED_LENGTH -> {
                    binding?.createPasswordInput?.isErrorEnabled = true
                    binding?.createPasswordInput?.error =
                        "The password must have at least 8 characters"
                }
                !text.matches(".*[A-Z].*".toRegex()) -> {
                    binding?.createPasswordInput?.isErrorEnabled = true
                    binding?.createPasswordInput?.error =
                        "The password must contain at least 1 upper-case character"
                }
                !text.matches(".*[a-z].*".toRegex()) -> {
                    binding?.createPasswordInput?.isErrorEnabled = true
                    binding?.createPasswordInput?.error =
                        "The password must contain at least 1 lower-case character"
                }
                !text.matches(".*[@#\$%^&+=].*".toRegex()) -> {
                    binding?.createPasswordInput?.isErrorEnabled = true
                    binding?.createPasswordInput?.error =
                        "The password must contain at least 1 number"
                }
                else -> {
                    binding?.createPasswordInput?.isErrorEnabled = false
                    binding?.createPasswordInput?.error = null
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
package com.grosianu.jobseeker.module.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.grosianu.jobseeker.databinding.FragmentLoginBinding
import com.grosianu.jobseeker.module.home.HomeActivity

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.loginBtn.setOnClickListener {
            val email: String = binding.loginEmailInputEdit.text.toString()
            val password: String = binding.loginPasswordInputEdit.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(
                    requireContext(),
                    "Please fill the required fields",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), OnCompleteListener {
                        if (it.isSuccessful) {
                            val intent = Intent(requireContext(), HomeActivity::class.java)
                            startActivity(intent)
                            requireActivity().finish()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Login unsuccessful",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            }
        }

        binding.createAccountBtn.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            this.findNavController().navigate(action)
        }
        binding.forgotPasswordBtn.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
            this.findNavController().navigate(action)
        }
    }
}
package com.grosianu.jobseeker.ui.login.register

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
import com.google.firebase.firestore.FirebaseFirestore
import com.grosianu.jobseeker.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.createAccountBtn.setOnClickListener {
            val email: String = binding.createEmailInputEdit.text.toString()
            val password: String = binding.createPasswordInputEdit.text.toString()

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(
                    requireContext(),
                    "Please fill the required fields",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity(), OnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(
                                requireContext(),
                                "Account successfully created",
                                Toast.LENGTH_LONG
                            ).show()
                            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                            this.findNavController().navigate(action)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Sign up unsuccessful",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            }
        }

        binding.loginBtn.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
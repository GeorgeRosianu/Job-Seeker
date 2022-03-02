package com.grosianu.jobseeker.module.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentHomeBinding
import com.grosianu.jobseeker.databinding.FragmentLoginBinding
import com.grosianu.jobseeker.module.login.StartupActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        binding.logoutBtn.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), StartupActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }
}
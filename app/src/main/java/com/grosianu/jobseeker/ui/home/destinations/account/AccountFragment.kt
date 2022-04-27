package com.grosianu.jobseeker.ui.home.destinations.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import coil.load
import com.google.firebase.auth.FirebaseAuth
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentAccountBinding
import com.grosianu.jobseeker.ui.login.StartupActivity
import com.grosianu.jobseeker.utils.DetailsDialog

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AccountViewModel by viewModels()

    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization()
    }

    private fun initialization() {
        setupViews()
    }

    private fun setupViews() {
        val username = auth.currentUser?.displayName?.substringBefore(" ").toString()
        val imageUri = auth.currentUser?.photoUrl

        if (username.isEmpty() || username == "null") {
            binding.greetingTextView.text = resources.getString(R.string.user_greeting_noname)
        } else {
            binding.greetingTextView.text = resources.getString(R.string.user_greeting, username)
        }
        if(imageUri == null) {
            binding.imageView.setImageResource(R.drawable.ic_broken_image)
            binding.imageView.setPadding(32)
        } else {
            binding.imageView.load(imageUri)
        }
        binding.addDetailsBtn.setOnClickListener {
            // TODO Navigate to addDetails
            openDialog()
        }
        binding.logoutBtn.setOnClickListener {
            logout()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun logout() {
        viewModel.auth.signOut()
        val intent = Intent(requireContext(), StartupActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun openDialog() {
        //val detailsDialog = DetailsDialog()
        //detailsDialog.display(parentFragmentManager)
        DetailsDialog().display(parentFragmentManager)
    }
}
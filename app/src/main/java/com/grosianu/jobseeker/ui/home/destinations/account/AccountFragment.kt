package com.grosianu.jobseeker.ui.home.destinations.account

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.view.doOnPreDraw
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.transition.MaterialContainerTransform
import com.google.firebase.auth.FirebaseAuth
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentAccountBinding
import com.grosianu.jobseeker.ui.home.HomeActivity
import com.grosianu.jobseeker.ui.home.HomeActivityViewModel
import com.grosianu.jobseeker.ui.home.destinations.account.viewModels.AccountViewModel
import com.grosianu.jobseeker.ui.login.StartupActivity
import com.grosianu.jobseeker.utils.DetailsDialog
import com.grosianu.jobseeker.utils.themeColor

class AccountFragment : Fragment() {

    private var binding: FragmentAccountBinding? = null
    private val sharedViewModel: HomeActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentAccountBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
    }

    private fun setupViews() {
        sharedViewModel.currentAccount.observe(viewLifecycleOwner) {
            setGreetingInfo()
        }

        setGreetingInfo()
        binding?.item1CardView?.setOnClickListener {
            val directions = AccountFragmentDirections.actionAccountFragmentToAccountDetailsFragment()
            findNavController().navigate(directions)
        }
        binding?.logoutBtn?.setOnClickListener {
            signUserOut()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun signUserOut() {
        sharedViewModel.signUserOut()
        val intent = Intent(requireContext(), StartupActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun setGreetingInfo() {
        val username = sharedViewModel.currentAccount.value?.displayName?.substringBefore(" ").toString()
        val imageUri = sharedViewModel.currentAccount.value?.imageUri

        if (username.isEmpty() || username == "null") {
            binding?.greetingTextView?.text = resources.getString(R.string.user_greeting_noname)
        } else {
            binding?.greetingTextView?.text = resources.getString(R.string.user_greeting, username)
        }

        if (imageUri.isNullOrEmpty()) {
            binding?.imageView?.setImageResource(R.drawable.ic_broken_image)
            binding?.imageView?.setPadding(32)
        } else {
            binding?.imageView?.load(imageUri)
            binding?.imageView?.setPadding(0)
        }
    }
}
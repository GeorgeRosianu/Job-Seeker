package com.grosianu.jobseeker.ui.home.destinations.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.grosianu.jobseeker.databinding.FragmentAccountBinding
import com.grosianu.jobseeker.ui.login.StartupActivity

class AccountFragment : Fragment() {

    // View binding
    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    // View Model
    private val viewModel: AccountViewModel by viewModels()

    // Firebase Firestore
    //private val database = Firebase.firestore

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

//        val query = database.collection("users")
//            .whereEqualTo(FieldPath.documentId(), auth.currentUser?.uid)

        binding.logoutBtn.setOnClickListener {
            viewModel.auth.signOut()
            val intent = Intent(requireContext(), StartupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "AccountFragment"
    }
}
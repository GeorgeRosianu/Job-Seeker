package com.grosianu.jobseeker.ui.home.destinations.applications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialElevationScale
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentApplicationsBinding
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.MyApplicationsFragment
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.MyPostsFragment

class ApplicationsFragment : Fragment() {

    private var _binding: FragmentApplicationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ApplicationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApplicationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init() {
        binding.viewPager.adapter = object : FragmentStateAdapter(requireActivity()) {
            override fun getItemCount(): Int {
                return 2
            }

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> {
                        MyApplicationsFragment.newInstance()
                    }
                    else -> {
                        MyPostsFragment.newInstance()
                    }
                }
            }
        }
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "My Applications"
                else -> "My Offers"
            }
        }.attach()
    }

//    fun navigateToCreate() {
//        this.apply {
//            exitTransition = MaterialElevationScale(false).apply {
//                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
//            }
//            reenterTransition = MaterialElevationScale(true).apply {
//                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
//            }
//        }
//    }

    companion object {
        private const val TAG = "ApplicationsFragment"
    }
}
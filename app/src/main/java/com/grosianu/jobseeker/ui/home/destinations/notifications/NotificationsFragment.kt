package com.grosianu.jobseeker.ui.home.destinations.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentNotificationsBinding
import com.grosianu.jobseeker.models.News
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.MyApplicationsFragmentDirections

class NotificationsFragment : Fragment(), NotificationsAdapter.NotificationsAdapterListener {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationsViewModel by viewModels()
    private val notificationsAdapter = NotificationsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization(view)
    }

    private fun initialization(view: View) {
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        updateRecycleView()
        setupViews()
    }

    private fun setupViews() {
        binding.lifecycleOwner = this

        viewModel.hasNews.observe(viewLifecycleOwner) {
            if (it) {
                binding.placeholderTextView.visibility = View.INVISIBLE
            } else {
                binding.placeholderTextView.visibility = View.VISIBLE
            }
        }

        binding.swipeView.setOnRefreshListener {
            refreshNewsList()
        }
    }

    private fun updateRecycleView() {
        viewModel.getContent()
        binding.viewModel = viewModel
        binding.recyclerView.adapter = notificationsAdapter
    }

    private fun refreshNewsList() {
        binding.swipeView.isRefreshing = false
        updateRecycleView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClicked(cardView: View, item: News) {
//        val newsCardDetailTransitionName = getString(R.string.news_card_detail_transition_name)
//        val extras = FragmentNavigatorExtras(cardView to newsCardDetailTransitionName)
//        exitTransition = MaterialElevationScale(false).apply {
//            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
//        }
//        reenterTransition = MaterialElevationScale(true).apply {
//            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
//        }
//
//        val directions = when (viewModel.news.value?.type) {
//            "NEW_APPLICATION" -> NotificationsFragmentDirections.actionNotificationsFragmentToApplicantsFragment()
//        }
//
//        val directions = MyApplicationsFragmentDirections.actionGlobalApplicationFragment(post.id.toString())
//        findNavController().navigate(directions, extras)
    }

    companion object {
        private const val TAG = "NOTIFICATION_FRAGMENT"
    }
}
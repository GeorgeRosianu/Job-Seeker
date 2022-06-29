package com.grosianu.jobseeker.ui.home.destinations.discover

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFade
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentDiscoverBinding
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.ui.home.HomeActivityViewModel
import com.grosianu.jobseeker.utils.Config
import com.grosianu.jobseeker.utils.FilterPopUpDialog
import com.grosianu.jobseeker.utils.RecommendationClient
import kotlinx.coroutines.launch

class DiscoverFragment : Fragment(), DiscoverAdapter.DiscoverAdapterListener {

    private var binding: FragmentDiscoverBinding? = null

    private val viewModel: DiscoverViewModel by viewModels()
    private val sharedViewModel: HomeActivityViewModel by activityViewModels()
    private val discoverAdapter = DiscoverAdapter(this)

    private var config = Config()
    private var client: RecommendationClient? = null

    private var asc: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentDiscoverBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        updateRecycleView()
        setupViews()
    }

    private fun setupViews() {
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner

            swipeView.setOnRefreshListener {
                refreshPostList()
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(p0: String?): Boolean {
                    if (p0 != null && p0.isNotEmpty()) {
                        searchPostList(p0)
                    } else {
                        updateRecycleView()
                    }
                    return true
                }

                override fun onQueryTextSubmit(p0: String?): Boolean {
                    searchView.clearFocus()
                    return true
                }
            })
            filterButton.setOnClickListener {
                filterPostList()
            }
            sortButton.setOnClickListener {
                sortPostList()
            }
        }
    }

    private fun updateRecycleView() {
        viewModel.getPostList()
        binding?.viewModel = viewModel
        binding?.recyclerView?.adapter = discoverAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onPostClicked(cardView: View, post: Post) {
        val postCardDetailTransitionName = getString(R.string.post_card_detail_transition_name)
        val extras = FragmentNavigatorExtras(cardView to postCardDetailTransitionName)
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        val directions = DiscoverFragmentDirections.actionGlobalOfferFragment(post.id.toString())
        findNavController().navigate(directions, extras)
    }

    override fun onPostLongPressed(post: Post): Boolean {
        TODO("Not yet implemented")
    }

    override fun onApplyClicked(post: Post) {
        val start = "discover"
        val isUserSetUp = sharedViewModel.isUserSetUp.value ?: false
        val hasResumes = sharedViewModel.hasResumes.value ?: false
        val directions = DiscoverFragmentDirections.actionGlobalApplySelectResumeFragment(post.id.toString(), start, isUserSetUp)

        if (hasResumes || isUserSetUp) {
            findNavController().navigate(directions)
        } else {
            alertMissingInfo()
        }
    }

    override fun onAddFavoriteClicked(view: View, post: Post) {
        viewModel.addToFavorite(post)
        binding?.viewModel = viewModel
    }

    override fun onStart() {
        super.onStart()

        exitTransition = MaterialFade()
        reenterTransition = MaterialFade()
    }

    override fun onResume() {
        super.onResume()

        exitTransition = MaterialFade()
        reenterTransition = MaterialFade()
    }

    override fun onPause() {
        super.onPause()

        exitTransition = MaterialFade()
        reenterTransition = MaterialFade()
    }

    override fun onStop() {
        super.onStop()

        exitTransition = MaterialFade()
        reenterTransition = MaterialFade()
    }

    private fun refreshPostList() {
        binding?.swipeView?.isRefreshing = false
        updateRecycleView()
    }

    private fun searchPostList(string: String) {
        viewModel.search(string)
        binding?.viewModel = viewModel
        binding?.recyclerView?.adapter = discoverAdapter
    }

    private fun filterPostList() {
        val array = resources.getStringArray(R.array.tags).sortedArray()
        val dialog = FilterPopUpDialog(array)
        dialog.show(parentFragmentManager, "Filter")

        setFragmentResultListener("requestKey") { _, bundle ->
            val result = bundle.getStringArrayList("bundleKey")

            if (result != null && !result.isNullOrEmpty()) {
                viewModel.filter(result)
                binding?.viewModel = viewModel
                binding?.recyclerView?.adapter = discoverAdapter
                binding?.recyclerView?.layoutManager?.scrollToPosition(0)
            } else {
                updateRecycleView()
            }
        }
    }

    private fun sortPostList() {
        viewModel.sort(asc)
        binding?.viewModel = viewModel
        binding?.recyclerView?.adapter = discoverAdapter
        binding?.recyclerView?.layoutManager?.scrollToPosition(0)
        asc = !asc
    }

    private fun alertMissingInfo() {
        AlertDialog.Builder(requireContext())
            .setTitle("Your profile is not set up!")
            .setMessage("Please complete your profile or upload a CV to apply.")
            .setPositiveButton("Ok") { dialog, _ -> dialog.cancel() }
            .show()
    }
}
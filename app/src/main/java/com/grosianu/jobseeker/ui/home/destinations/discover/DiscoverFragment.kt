package com.grosianu.jobseeker.ui.home.destinations.discover

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentDiscoverBinding
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.utils.FilterPopUpDialog

class DiscoverFragment : Fragment(), DiscoverAdapter.DiscoverAdapterListener {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DiscoverViewModel by viewModels()
    private val discoverAdapter = DiscoverAdapter(this)

    private var asc: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
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
        binding.swipeView.setOnRefreshListener {
            refreshPostList()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null && p0.isNotEmpty()) {
                    searchPostList(p0)
                } else {
                    updateRecycleView()
                }
                return true
            }

            override fun onQueryTextSubmit(p0: String?): Boolean {
                binding.searchView.clearFocus()
                return true
            }
        })

        binding.filterButton.setOnClickListener {
            filterPostList()
        }
        binding.sortButton.setOnClickListener {
            sortPostList()
        }
    }

    private fun setupViewModel() {
        viewModel.getPostList()
        binding.viewModel = viewModel
    }

    private fun updateRecycleView() {
        viewModel.getPostList()
        binding.viewModel = viewModel
        binding.recyclerView.adapter = discoverAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        val directions = DiscoverFragmentDirections.actionGlobalApplySelectResumeFragment(post.id.toString(), start)
        findNavController().navigate(directions)
    }

    override fun onAddFavoriteClicked(view: View, post: Post) {

    }

    private fun refreshPostList() {
        binding.swipeView.isRefreshing = false
        updateRecycleView()
    }

    private fun searchPostList(string: String) {
        viewModel.search(string)
        binding.viewModel = viewModel
        binding.recyclerView.adapter = discoverAdapter
    }

    private fun filterPostList() {
        val array = resources.getStringArray(R.array.tags).sortedArray()
        val dialog = FilterPopUpDialog(array)
        dialog.show(parentFragmentManager, "Filter")

        setFragmentResultListener("requestKey") { _, bundle ->
            val result = bundle.getStringArrayList("bundleKey")

            if (result != null && !result.isNullOrEmpty()) {
                viewModel.filter(result)
                binding.viewModel = viewModel
                binding.recyclerView.adapter = discoverAdapter
                binding.recyclerView.layoutManager?.scrollToPosition(0)
            } else {
                updateRecycleView()
            }
        }
    }

    private fun sortPostList() {
        viewModel.sort(asc)
        binding.viewModel = viewModel
        binding.recyclerView.adapter = discoverAdapter
        binding.recyclerView.layoutManager?.scrollToPosition(0)
        asc = !asc
    }
}
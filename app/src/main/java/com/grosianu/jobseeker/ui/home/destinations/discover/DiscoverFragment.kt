package com.grosianu.jobseeker.ui.home.destinations.discover

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentDiscoverBinding
import com.grosianu.jobseeker.databinding.ItemPostDiscoverBinding
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.utils.PopUpDialog
import java.util.*

class DiscoverFragment : Fragment(), DiscoverAdapter.DiscoverAdapterListener {

    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    private val itemBinding: ItemPostDiscoverBinding? = null

    private val viewModel: DiscoverViewModel by viewModels()
    private val discoverAdapter = DiscoverAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPostList()
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.recyclerView.adapter = discoverAdapter
        binding.swipeView.setOnRefreshListener {
            updatePostList()
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(p0: String?): Boolean {
                if (p0 != null && p0.isNotEmpty()) {
                    searchPostList(p0)
                } else {
                    updatePostList()
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

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPostClicked(cardView: View, application: Application) {
        val postCardDetailTransitionName = getString(R.string.post_card_detail_transition_name)
        val extras = FragmentNavigatorExtras(cardView to postCardDetailTransitionName)
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        //val directions = DiscoverFragmentDirections.actionDiscoverFragmentToEditPostFragment(application.id.toString())
        //findNavController().navigate(directions, extras)
    }

    override fun onPostLongPressed(application: Application): Boolean {
        TODO("Not yet implemented")
    }

    override fun onApplyClicked(application: Application) {
        viewModel.userAddApplicant(application.id.toString())
        itemBinding?.applyBtn?.isEnabled = false
    }

    private fun updatePostList() {
        binding.swipeView.isRefreshing = false

        viewModel.getPostList()
        binding.viewModel = viewModel
        binding.recyclerView.adapter = discoverAdapter
    }

    private fun searchPostList(string: String) {
        viewModel.search(string)
        binding.viewModel = viewModel
        binding.recyclerView.adapter = discoverAdapter
    }

    private fun filterPostList() {
        val array = resources.getStringArray(R.array.popup_array)
        PopUpDialog(array).show(parentFragmentManager, "dialog")
    }

    private fun sortPostList() {

    }
}
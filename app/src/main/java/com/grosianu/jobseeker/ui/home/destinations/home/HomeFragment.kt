package com.grosianu.jobseeker.ui.home.destinations.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.grosianu.jobseeker.databinding.FragmentHomeBinding
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.ui.home.destinations.home.adapters.ApplicationsAdapter
import com.grosianu.jobseeker.ui.home.destinations.home.adapters.PostsAdapter
import com.grosianu.jobseeker.ui.home.destinations.home.viewModels.HomeViewModel

class HomeFragment : Fragment(), PostsAdapter.PostsAdapterListener,
    ApplicationsAdapter.ApplicationsAdapterListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val postsAdapter = PostsAdapter(this)
    private val applicationsAdapter = ApplicationsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization()
    }

    private fun initialization() {
        setupViews()
        updateRecycleView()
    }

    private fun setupViews() {
        binding.lifecycleOwner = this
        binding.postsBtn.setOnClickListener {
            navigateToPosts()
        }
        binding.applicationsBtn.setOnClickListener {
            navigateToApplications()
        }
    }

    private fun setupViewModel() {
        viewModel.getPostList()
        viewModel.getApplicationList()
        binding.viewModel = viewModel
    }

    private fun updateRecycleView() {
        viewModel.getPostList()
        viewModel.getApplicationList()
        binding.viewModel = viewModel
        binding.postsRecyclerView.adapter = postsAdapter
        binding.applicationsRecyclerView.adapter = applicationsAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPostClicked(cardView: View, post: Post) {
        val directions =
            HomeFragmentDirections.actionHomeFragmentToEditPostFragment(post.id.toString())
        findNavController().navigate(directions)
    }

    override fun onPostLongPressed(post: Post): Boolean {
        TODO("Not yet implemented")
    }

    override fun onApplicationClicked(cardView: View, post: Post) {
        val directions =
            HomeFragmentDirections.actionGlobalApplicationFragment(post.id.toString())
        findNavController().navigate(directions)
    }

    override fun onApplicationLongPressed(post: Post): Boolean {
        TODO("Not yet implemented")
    }

    private fun navigateToPosts() {
        val directions = HomeFragmentDirections.actionHomeFragmentToMyPostsFragment()
        findNavController().navigate(directions)
    }

    private fun navigateToApplications() {
        val directions = HomeFragmentDirections.actionHomeFragmentToMyApplicationsFragment()
        findNavController().navigate(directions)
    }

    private fun setFadeTransition() {
        enterTransition = MaterialFadeThrough()
        returnTransition = MaterialFadeThrough()
        reenterTransition = MaterialFadeThrough()
        exitTransition = MaterialFadeThrough()
    }
}
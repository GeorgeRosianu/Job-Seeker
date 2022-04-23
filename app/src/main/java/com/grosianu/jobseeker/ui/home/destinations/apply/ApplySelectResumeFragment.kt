package com.grosianu.jobseeker.ui.home.destinations.apply

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.grosianu.jobseeker.databinding.FragmentApplyChooseResumeBinding
import com.grosianu.jobseeker.models.Resume
import com.grosianu.jobseeker.ui.home.destinations.apply.adapters.ApplySelectResumeAdapter
import com.grosianu.jobseeker.ui.home.destinations.apply.viewModels.ApplySelectResumeViewModel
import com.grosianu.jobseeker.ui.home.destinations.discover.DiscoverFragmentDirections

class ApplySelectResumeFragment :
    Fragment(), ApplySelectResumeAdapter.ApplySelectResumeAdapterListener {

    private var _binding: FragmentApplyChooseResumeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ApplySelectResumeViewModel by viewModels()
    private val adapter = ApplySelectResumeAdapter(this)

    private val args: ApplySelectResumeFragmentArgs by navArgs()

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = auth.currentUser?.displayName?.substringBefore(" ").toString()
        if (username.isEmpty() || username == "null") {
//            val backDirections = DiscoverFragmentDirections.actionGlobalAccountFragment()
//            val directions = ApplySelectResumeFragmentDirections.actionApplySelectResumeFragmentToDiscoverFragment()
//            findNavController().navigate(directions)
//            findNavController().navigate(backDirections)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApplyChooseResumeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization()
    }

    private fun initialization() {
        updateRecyclerView()
        setupViews()
    }

    private fun setupViews() {
        binding.lifecycleOwner = this
        binding.cancelIcon.setOnClickListener {
            navigateBack()
        }
        binding.navigationIcon.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupViewModel() {
        viewModel.getResumeList()
        binding.viewModel = viewModel
    }

    private fun updateRecyclerView() {
        viewModel.getResumeList()
        binding.viewModel = viewModel
        binding.recyclerView.adapter = adapter
    }

    override fun onResumeClicked(resume: Resume) {
        val directions = ApplySelectResumeFragmentDirections.actionApplySelectResumeFragmentToApplyWriteMessageFragment(resume.id.toString(), args.postId, args.start)
        findNavController().navigate(directions)
    }

    private fun navigateBack() {
        val directions = if (args.start == "posts") {
            ApplySelectResumeFragmentDirections.actionApplySelectResumeFragmentToApplicationFragment(
                args.postId
            )
        } else {
            ApplySelectResumeFragmentDirections.actionApplySelectResumeFragmentToDiscoverFragment()
        }
        findNavController().navigate(directions)
    }
}
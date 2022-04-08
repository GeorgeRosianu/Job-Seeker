package com.grosianu.jobseeker.ui.home.destinations

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat.canScrollVertically
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.MaterialContainerTransform
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentApplicationBinding
import com.grosianu.jobseeker.utils.themeColor

class ApplicationFragment : Fragment() {

    private var _binding: FragmentApplicationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ApplicationViewModel by viewModels()
    private val args: ApplicationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentApplicationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSharedTransition()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization()
    }

    private fun initialization() {
        setupViewModel()
        setupViews()
    }

    private fun setupViews() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.navigationIcon.setOnClickListener {
            findNavController().navigateUp()
        }
//        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            val x = scrollY - oldScrollY
//            if (x > 0) {
//                binding.fabApply.shrink()
//            } else if (x < 0) {
//                binding.fabApply.extend()
//            }
//        })
    }

    private fun setupViewModel() {
        viewModel.getPost(args.postId)
        binding.viewModel = viewModel
    }

    private fun setupSharedTransition() {
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment_home
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
    }
}
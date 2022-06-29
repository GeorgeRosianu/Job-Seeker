package com.grosianu.jobseeker.ui.home.destinations.resume

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentResumeBinding
import com.grosianu.jobseeker.models.Resume
import com.grosianu.jobseeker.ui.home.HomeActivityViewModel

class ResumeFragment : Fragment(), ResumeAdapter.ResumeAdapterListener {

    private var binding: FragmentResumeBinding? = null

    private val viewModel: ResumeViewModel by viewModels()
    private val sharedViewModel: HomeActivityViewModel by activityViewModels()
    private val resumeAdapter = ResumeAdapter(this)

    private var isFabOpen: Boolean = false;

    private lateinit var fileImageUri: Uri
    private lateinit var fileUri: Uri
    private var fileFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                fileUri = uri
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    binding?.fabCheck?.isVisible == true -> {
                        binding?.fabCheck?.hide()
                        binding?.fab?.show()
                    }
                    isFabOpen -> {
                        hideFabMenu()
                    }
                    else -> {
                        findNavController().navigateUp()
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentResumeBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateRecyclerView()
        setupViews()
    }

    private fun setupViews() {
        binding?.lifecycleOwner = viewLifecycleOwner

        viewModel.hasResume.observe(viewLifecycleOwner) {
            if (it) {
                binding?.placeholderTextView?.visibility = View.INVISIBLE
            } else {
                binding?.placeholderTextView?.visibility = View.VISIBLE
            }
        }

        binding?.fab?.apply {
            setOnClickListener {
                if (!isFabOpen) {
                    showFabMenu()
                } else {
                    hideFabMenu()
                }
            }
        }
        binding?.fabCreate?.setOnClickListener {

        }
        binding?.fabUpload?.setOnClickListener {
            getFileFromGallery()
            hideFabMenu()
            binding?.fab?.visibility = View.INVISIBLE
            binding?.fabCheck?.show()
        }
        binding?.fabCheck?.setOnClickListener {
            binding?.fabCheck?.hide()
            binding?.fab?.show()

            if (this::fileUri.isInitialized) {
                val fileName = getFileNameFromUri(requireContext(), fileUri)
                viewModel.updateFirestoreData(fileUri, fileName.toString())
            }
        }
        binding?.fabBgLayout?.setOnClickListener {
            hideFabMenu()
        }
    }

    private fun updateRecyclerView() {
        viewModel.getResumeList()
        binding?.viewModel = viewModel
        binding?.recyclerView?.adapter = resumeAdapter
    }

    private fun getFileFromGallery() = fileFromGallery.launch("application/pdf")

    private fun getFileNameFromUri(context: Context, uri: Uri): String? {
        val fileName: String?
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val index = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        fileName = cursor?.getString(index!!)
        cursor?.close()
        return fileName
    }

    private fun hideFabMenu() {
        binding?.apply {

            fabBgLayout.visibility = View.GONE
            fab.animate().rotation(0F)

            fabCreate.animate().translationY(0F)
            createActionCard.animate().translationY(0F)

            fabUpload.animate().translationY(0F)
            uploadActionCard.animate().translationY(0F)

            fabUpload.animate().translationY(0F).setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {
                    if (!isFabOpen) {
                        uploadActionCard.visibility = View.GONE
                        createActionCard.visibility = View.GONE
                    }
                }

                override fun onAnimationEnd(animator: Animator) {
                    if (!isFabOpen) {
                        fabUpload.visibility = View.GONE
                        fabCreate.visibility = View.GONE
                        uploadActionCard.visibility = View.GONE
                        createActionCard.visibility = View.GONE
                    }
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })
        }

        isFabOpen = false
    }

    private fun showFabMenu() {
        binding?.apply {

            fabBgLayout.visibility = View.VISIBLE
            fab.animate().rotationBy(135F)

            fabCreate.visibility = View.VISIBLE
            fabUpload.visibility = View.VISIBLE

            fabCreate.animate().translationY(-resources.getDimension(R.dimen.standard_55))
            createActionCard.animate().translationY(-resources.getDimension(R.dimen.standard_55))

            fabUpload.animate().translationY(-resources.getDimension(R.dimen.standard_105))
            uploadActionCard.animate().translationY(-resources.getDimension(R.dimen.standard_105))

            uploadActionCard.animate().translationY(-resources.getDimension(R.dimen.standard_105))
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {}
                    override fun onAnimationEnd(animator: Animator) {
                        if (isFabOpen) {
                            uploadActionCard.visibility = View.VISIBLE
                            createActionCard.visibility = View.VISIBLE
                        }
                    }

                    override fun onAnimationCancel(animator: Animator) {}
                    override fun onAnimationRepeat(animator: Animator) {}
                })
        }

        isFabOpen = true
    }

    override fun onResumeClicked(cardView: View, resume: Resume) {
        val directions = ResumeFragmentDirections.actionGlobalPdfViewFragment(resume.url.toString())
        findNavController().navigate(directions)
    }

    override fun onResumeLongPressed(resume: Resume): Boolean {
        TODO("Not yet implemented")
    }
}
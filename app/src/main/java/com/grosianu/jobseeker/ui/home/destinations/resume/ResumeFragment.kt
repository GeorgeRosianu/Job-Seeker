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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentResumeBinding
import com.grosianu.jobseeker.models.Resume
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class ResumeFragment : Fragment(), ResumeAdapter.ResumeAdapterListener {

    private var _binding: FragmentResumeBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val viewModel: ResumeViewModel by viewModels()
    private val resumeAdapter = ResumeAdapter(this)

    private var isFabOpen: Boolean = false;

    private lateinit var fileImageUri: Uri
    private lateinit var fileUri: Uri
    private var fileFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                fileUri = uri
                // TODO
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.fabCheck.isVisible) {
                    binding.fabCheck.hide()
                    binding.fab.show()
                } else if (isFabOpen) {
                    hideFabMenu()
                } else {
                    findNavController().navigateUp()
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
        _binding = FragmentResumeBinding.inflate(inflater, container, false)
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
        binding.fab.apply {
            setOnClickListener {
                if (!isFabOpen) {
                    showFabMenu()
                } else {
                    hideFabMenu()
                }
            }
        }
        binding.fabCreate.setOnClickListener {

        }
        binding.fabUpload.setOnClickListener {
            getFileFromGallery()
            hideFabMenu()
            binding.fab.visibility = View.INVISIBLE
            binding.fabCheck.show()
        }
        binding.fabCheck.setOnClickListener {
            binding.fabCheck.hide()
            binding.fab.show()

            if (this::fileUri.isInitialized) {
                uploadFileToFirestore()
            }
        }
        binding.fabBgLayout.setOnClickListener {
            hideFabMenu()
        }
    }

    private fun setupViewModel() {
        viewModel.getResumeList()
        binding.viewModel = viewModel
    }

    private fun updateRecyclerView() {
        viewModel.getResumeList()
        binding.viewModel = viewModel
        binding.recyclerView.adapter = resumeAdapter
    }

    private fun getFileFromGallery() = fileFromGallery.launch("application/pdf")

    private fun uploadFileToFirestore() {
        val fileName = UUID.randomUUID().toString()
        val storageRef = storage.getReference("resumes/$fileName")

        storageRef.putFile(fileUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    updateFirestoreUserData(it.toString())
                }
            }
    }

    private fun updateFirestoreUserData(firestoreFileUri: String) {
        val id: String = UUID.randomUUID().toString()
        val url: String = firestoreFileUri
        val owner = auth.currentUser?.uid.toString()
        val title: String?
        val applicationTitle = auth.currentUser?.displayName.toString().replace(" ", "_").lowercase()
        val dateCreated = getDate()

        fileUri.let { uri ->
            val filename = getFileNameFromUri(requireContext(), uri)
            title = filename
        }

        val resume = Resume(
            id,
            url,
            owner,
            title,
            applicationTitle,
            null,
            dateCreated,
        )

        db.collection("resumes").document(id)
            .set(resume)
            .addOnSuccessListener {
            }
    }

    private fun getPdfPreviewImage(filePath: File): String {
        val fileDescriptor =
            ParcelFileDescriptor.open(filePath, ParcelFileDescriptor.MODE_READ_ONLY)
        val renderer = PdfRenderer(fileDescriptor)
        val page = renderer.openPage(0)

        val bao = ByteArrayOutputStream()
        val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao)
        bitmap.recycle()
        val byteArray = bao.toByteArray()

        return Base64.getEncoder().encodeToString(byteArray)
    }

    private fun getFileNameFromUri(context: Context, uri: Uri): String? {
        val fileName: String?
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val index = cursor?.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        fileName = cursor?.getString(index!!)
        cursor?.close()
        return fileName
    }

    private fun getCurrentMonth(month: String): String {
        return when (month) {
            "01" -> "Jan"
            "02" -> "Feb"
            "03" -> "Mar"
            "04" -> "Apr"
            "05" -> "May"
            "06" -> "Jun"
            "07" -> "Jul"
            "08" -> "Aug"
            "09" -> "Sep"
            "10" -> "Oct"
            "11" -> "Nov"
            "12" -> "Dec"
            else -> {
                ""
            }
        }
    }

    private fun getDate(): String {
        val sdf = SimpleDateFormat("dd, yyyy", Locale.getDefault())
        val date = sdf.format(Date()).toString()
        val dateFormat = SimpleDateFormat("MM", Locale.getDefault())
        val month = getCurrentMonth(dateFormat.format(Date()).toString())

        return "$month $date"
    }

    private fun hideFabMenu() {
        binding.apply {

            fabBgLayout.visibility = View.GONE
            fab.animate().rotation(0F)

            fabCreate.animate().translationY(0F)
            //createActionText.animate().translationY(0F)
            createActionCard.animate().translationY(0F)

            fabUpload.animate().translationY(0F)
            //uploadActionText.animate().translationY(0F)
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
        binding.apply {

            fabBgLayout.visibility = View.VISIBLE
            fab.animate().rotationBy(135F)

            fabCreate.visibility = View.VISIBLE
            fabUpload.visibility = View.VISIBLE

            fabCreate.animate().translationY(-resources.getDimension(R.dimen.standard_55))
            //createActionText.animate().translationY(-resources.getDimension(R.dimen.standard_55))
            createActionCard.animate().translationY(-resources.getDimension(R.dimen.standard_55))

            fabUpload.animate().translationY(-resources.getDimension(R.dimen.standard_105))
            //uploadActionText.animate().translationY(-resources.getDimension(R.dimen.standard_105))
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
        // TODO Navigate to Activity/Fragment (PdfViewFragment)
        val directions = ResumeFragmentDirections.actionGlobalPdfViewFragment(resume.url.toString())
        findNavController().navigate(directions)
    }

    override fun onResumeLongPressed(resume: Resume): Boolean {
        TODO("Not yet implemented")
    }
}
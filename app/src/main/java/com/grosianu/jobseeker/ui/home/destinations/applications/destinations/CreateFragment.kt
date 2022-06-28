package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import com.google.android.material.transition.MaterialContainerTransform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentCreateBinding
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.ui.home.HomeActivityViewModel
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels.CreateViewModel
import com.grosianu.jobseeker.utils.themeColor
import java.util.*


class CreateFragment : Fragment() {

    private var binding: FragmentCreateBinding? = null
    private val viewModel: CreateViewModel by viewModels()
    private val auth = FirebaseAuth.getInstance()

    private lateinit var imageUri: Uri
    private var imageFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = uri
                binding?.apply {
                    addImageBtn.setImageURI(uri)
                    addImageTextView.visibility = View.GONE
                    addImageIcon.visibility = View.GONE
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentCreateBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupArrays()
        setupFabTransition()
        setupViews()
        fieldValidations()
    }

    private fun setupViews() {
        binding?.addBtn?.setOnClickListener {
            if (isFormValid()) {
                uploadData()
                findNavController().navigateUp()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Fields in the form are invalid.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding?.addImageBtn?.setOnClickListener {
            getImageFromGallery()
        }
        binding?.navigationIcon?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getImageFromGallery() = imageFromGallery.launch("image/*")

    private fun uploadData() {
        val imageString = if (this::imageUri.isInitialized) {
            imageUri.toString()
        } else {
            ""
        }

        val filename = UUID.randomUUID().toString()
        val tagsString = binding?.tagsEdit?.text.toString().trim().trimEnd {it <= ','}

        val post = Post(
            id = UUID.randomUUID().toString(),
            owner = auth.currentUser?.uid.toString(),
            title = binding?.titleEdit?.text.toString(),
            company = binding?.companyEdit?.text.toString(),
            industry = binding?.industryEdit?.text.toString(),
            salary = binding?.salaryEdit?.text.toString().toDouble(),
            level = binding?.requirementsLevelEdit?.text.toString(),
            experience = binding?.requirementsExperienceEdit?.text.toString(),
            location = binding?.requirementsLocationEdit?.text.toString(),
            otherRequirements = binding?.requirementsEdit?.text.toString(),
            description = binding?.descriptionEdit?.text.toString(),
            image = imageString,
            imageId = filename,
            tags = getArrayFromString(tagsString) as ArrayList<String>,
            applicants = null,
            confirmedApplicants = null
        )

        viewModel.updateFirestoreData(post)
        findNavController().navigateUp()
    }

    private fun setupFabTransition() {
        enterTransition = MaterialContainerTransform().apply {
            startView = requireActivity().findViewById(R.id.fab_add_offer)
            endView = binding?.createCardView
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            containerColor = requireContext().themeColor(R.attr.colorSurface)
            startContainerColor = requireContext().themeColor(R.attr.colorSecondary)
            endContainerColor = requireContext().themeColor(R.attr.colorSurface)
        }
        returnTransition = Slide().apply {
            duration = resources.getInteger(R.integer.motion_duration_medium).toLong()
            addTarget(R.id.create_card_view)
        }
    }

    private fun setupArrays() {
        val industries = resources.getStringArray(R.array.industries)
        var arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, industries)
        binding?.industryEdit?.setAdapter(arrayAdapter)

        val levels = resources.getStringArray(R.array.levels)
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, levels)
        binding?.requirementsLevelEdit?.setAdapter(arrayAdapter)

        val tags = resources.getStringArray(R.array.tags).sorted()
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, tags)
        binding?.tagsEdit?.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        binding?.tagsEdit?.threshold = 1
        binding?.tagsEdit?.setAdapter(arrayAdapter)
    }

    private fun getArrayFromString(string: String): List<String> {
        return string.split(",")
    }

    private fun isFormValid(): Boolean {
        if (binding?.title?.isErrorEnabled == true ||
            binding?.company?.isErrorEnabled == true ||
            binding?.industry?.isErrorEnabled == true ||
            binding?.requirementsLevel?.isErrorEnabled == true ||
            binding?.requirementsExperience?.isErrorEnabled == true ||
            binding?.requirementsLocation?.isErrorEnabled == true ||
            !this::imageUri.isInitialized
        ) {
            return false
        } else if (
            binding?.titleEdit?.text.isNullOrEmpty() ||
            binding?.companyEdit?.text.isNullOrEmpty() ||
            binding?.industryEdit?.text.isNullOrEmpty() ||
            binding?.requirementsLevelEdit?.text.isNullOrEmpty() ||
            binding?.requirementsExperienceEdit?.text.isNullOrEmpty() ||
            binding?.requirementsLocationEdit?.text.isNullOrEmpty()
        ) {
            return false
        }
        return true
    }

    private fun fieldValidations() {
        binding?.titleEdit?.doOnTextChanged { text, _, _, _ ->
            when {
                text.isNullOrEmpty() -> {
                    binding?.title?.isErrorEnabled = true
                    binding?.title?.error = "Title field cannot be empty"
                }
                else -> {
                    binding?.title?.isErrorEnabled = false
                    binding?.title?.error = null
                }
            }
        }
        binding?.companyEdit?.doOnTextChanged { text, _, _, _ ->
            when {
                text.isNullOrEmpty() -> {
                    binding?.company?.isErrorEnabled = true
                    binding?.company?.error = "Company field cannot be empty"
                }
                else -> {
                    binding?.company?.isErrorEnabled = false
                    binding?.company?.error = null
                }
            }
        }
        binding?.industryEdit?.doOnTextChanged { text, _, _, _ ->
            when {
                text.isNullOrEmpty() -> {
                    binding?.industry?.isErrorEnabled = true
                    binding?.industry?.error = "Industry field cannot be empty"
                }
                else -> {
                    binding?.industry?.isErrorEnabled = false
                    binding?.industry?.error = null
                }
            }
        }
        binding?.requirementsLevelEdit?.doOnTextChanged { text, _, _, _ ->
            when {
                text.isNullOrEmpty() -> {
                    binding?.requirementsLevel?.isErrorEnabled = true
                    binding?.requirementsLevel?.error = "Level field cannot be empty"
                }
                else -> {
                    binding?.requirementsLevel?.isErrorEnabled = false
                    binding?.requirementsLevel?.error = null
                }
            }
        }
        binding?.requirementsExperienceEdit?.doOnTextChanged { text, _, _, _ ->
            when {
                text.isNullOrEmpty() -> {
                    binding?.requirementsExperience?.isErrorEnabled = true
                    binding?.requirementsExperience?.error = "Experience field cannot be empty"
                }
                else -> {
                    binding?.requirementsExperience?.isErrorEnabled = false
                    binding?.requirementsExperience?.error = null
                }
            }
        }
        binding?.requirementsLocationEdit?.doOnTextChanged { text, _, _, _ ->
            when {
                text.isNullOrEmpty() -> {
                    binding?.requirementsLocation?.isErrorEnabled = true
                    binding?.requirementsLocation?.error = "Location field cannot be empty"
                }
                else -> {
                    binding?.requirementsLocation?.isErrorEnabled = false
                    binding?.requirementsLocation?.error = null
                }
            }
        }
    }
}
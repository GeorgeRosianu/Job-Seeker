package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentEditPostBinding
import com.grosianu.jobseeker.models.Post
import com.grosianu.jobseeker.ui.home.destinations.applications.destinations.viewModels.EditPostViewModel
import java.util.*

class EditPostFragment : Fragment() {

    private var binding: FragmentEditPostBinding? = null

    private val viewModel: EditPostViewModel by viewModels()
    private val args: EditPostFragmentArgs by navArgs()

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

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
        val fragmentBinding = FragmentEditPostBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupArrays()
        fieldValidations()

        viewModel.getPost(args.postId)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = viewLifecycleOwner

        binding?.addBtn?.setOnClickListener {
            if (!isFormValid()) {
                Toast.makeText(
                    requireContext(),
                    "Fields in the form are invalid.",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }

            if (this::imageUri.isInitialized) {
                updateDataWithImage()
            } else {
                updateDataToFirestore()
            }
            findNavController().navigateUp()
        }
        binding?.addImageBtn?.setOnClickListener {
            getImageFromGallery()
        }
        binding?.navigationIcon?.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getImageFromGallery() = imageFromGallery.launch("image/*")

    private fun updateDataWithImage() {
        val storageReference = storage.getReference("images/${viewModel.post.value?.imageId}")
        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {
                    val uri = it.toString()
                    updateDataToFirestoreWithImage(uri)
                }
            }
            .addOnFailureListener {
            }
    }

    private fun updateDataToFirestore() {
        val id: String = viewModel.post.value?.id!!
        val title: String = binding?.titleEdit?.text.toString()
        val company: String = binding?.companyEdit?.text.toString()
        val industry: String = binding?.industryEdit?.text.toString()
        val salary: Double = binding?.salaryEdit?.text.toString().toDouble()
        val level: String = binding?.requirementsLevelEdit?.text.toString()
        val experience: String = binding?.requirementsExperienceEdit?.text.toString()
        val location: String = binding?.requirementsLocationEdit?.text.toString()
        val otherRequirements: String = binding?.requirementsEdit?.text.toString()
        val description: String = binding?.descriptionEdit?.text.toString()
        val tagsString = binding?.tagsEdit?.text.toString().trim().trimEnd {it <= ','}
        val tags: ArrayList<String> = getArrayFromString(tagsString) as ArrayList<String>

        val dbRef = db.collection("posts").document(id)

        dbRef.update("title", title)
            .addOnSuccessListener { binding?.titleEdit?.text = null }
        dbRef.update("company", company)
            .addOnSuccessListener { binding?.companyEdit?.text = null }
        dbRef.update("description", description)
            .addOnSuccessListener { binding?.descriptionEdit?.text = null }
        dbRef.update("experience", experience)
            .addOnSuccessListener { binding?.requirementsExperienceEdit?.text = null }
        dbRef.update("salary", salary)
            .addOnSuccessListener { binding?.salaryEdit?.text = null }
        dbRef.update("tags", tags)
            .addOnSuccessListener { binding?.tagsEdit?.text = null }
        dbRef.update("industry", industry)
            .addOnSuccessListener { binding?.companyEdit?.text = null }
        dbRef.update("level", level)
            .addOnSuccessListener { binding?.requirementsLevelEdit?.text = null }
        dbRef.update("location", location)
            .addOnSuccessListener { binding?.requirementsLocationEdit?.text = null }
        dbRef.update("otherRequirements", otherRequirements)
            .addOnSuccessListener { binding?.requirementsEdit?.text = null }

    }

    private fun updateDataToFirestoreWithImage(image: String) {
        val id: String = viewModel.post.value?.id!!
        val owner: String = viewModel.post.value?.owner!!
        val title: String = binding?.titleEdit?.text.toString()
        val company: String = binding?.companyEdit?.text.toString()
        val industry: String = binding?.industryEdit?.text.toString()
        val salary: Double = binding?.salaryEdit?.text.toString().toDouble()
        val level: String = binding?.requirementsLevelEdit?.text.toString()
        val experience: String = binding?.requirementsExperienceEdit?.text.toString()
        val location: String = binding?.requirementsLocationEdit?.text.toString()
        val otherRequirements: String = binding?.requirementsEdit?.text.toString()
        val description: String = binding?.descriptionEdit?.text.toString()
        val tagsString = binding?.tagsEdit?.text.toString().trim().trimEnd {it <= ','}
        val tags: ArrayList<String> = getArrayFromString(tagsString) as ArrayList<String>

        val application = Post(
            id,
            owner,
            title,
            company,
            industry,
            salary,
            level,
            experience,
            location,
            otherRequirements,
            description,
            tags,
            image,
            viewModel.post.value?.imageId,
            viewModel.post.value?.applicants,
            viewModel.post.value?.confirmedApplicants,
        )

        db.collection("posts").document(id)
            .set(application)
            .addOnSuccessListener {
                binding?.titleEdit?.text = null
                binding?.companyEdit?.text = null
                binding?.industryEdit?.text = null
                binding?.salaryEdit?.text = null
                binding?.requirementsLevelEdit?.text = null
                binding?.requirementsExperienceEdit?.text = null
                binding?.requirementsLocationEdit?.text = null
                binding?.requirementsEdit?.text = null
                binding?.descriptionEdit?.text = null
                binding?.tagsEdit?.text = null
                binding?.addImageBtn?.setImageURI(null)
                binding?.addImageIcon?.setImageResource(R.drawable.ic_round_add_24)
                binding = null
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
            binding?.requirementsLocation?.isErrorEnabled == true
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
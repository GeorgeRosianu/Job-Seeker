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
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.transition.Slide
import com.google.android.material.transition.MaterialContainerTransform
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentCreateBinding
import com.grosianu.jobseeker.models.Application
import com.grosianu.jobseeker.utils.themeColor
import java.util.*


class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private lateinit var imageUri: Uri

    private var imageFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = uri
                binding.postImage.setImageURI(uri)
                binding.addImageBtn.visibility = View.INVISIBLE
                binding.addImageTextView.visibility = View.GONE
                binding.postImage.visibility = View.VISIBLE
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupArrays()
        setupFabTransition()

        binding.addBtn.setOnClickListener {
            if (binding.titleEdit.text.isNullOrEmpty() ||
                binding.companyEdit.text.isNullOrEmpty() ||
                binding.descriptionEdit.text.isNullOrEmpty()
            ) {

                Toast.makeText(
                    requireContext(),
                    "Please fill in the required data.",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setOnClickListener
            }

            uploadData()
            findNavController().navigateUp()
        }
        binding.addImageBtn.setOnClickListener {
            getImageFromGallery()
        }
        binding.navigationIcon.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getImageFromGallery() = imageFromGallery.launch("image/*")

    private fun uploadData() {
        val fileName = UUID.randomUUID().toString()
        val storageReference = storage.getReference("images/$fileName")

        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                binding.postImage.setImageURI(null)
                storageReference.downloadUrl.addOnSuccessListener {
                    uploadDataToFirestore(it.toString())
                }
            }
            .addOnFailureListener {
            }
    }

    private fun uploadDataToFirestore(image: String) {
        val id: String = UUID.randomUUID().toString()
        val owner: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val title: String = binding.titleEdit.text.toString()
        val company: String = binding.companyEdit.text.toString()
        val industry: String = binding.industryEdit.text.toString()
        val salary: Double = binding.salaryEdit.text.toString().toDouble()
        val level: String = binding.salaryEdit.text.toString()
        val experience: String = binding.requirementsExperienceEdit.text.toString()
        val location: String = binding.requirementsLocationEdit.text.toString()
        val otherRequirements: String = binding.requirementsEdit.text.toString()
        val description: String = binding.descriptionEdit.text.toString()
        val tagsString = binding.tagsEdit.text.toString().trim().trimEnd {it <= ','}
        val tags: ArrayList<String> = getArrayFromString(tagsString) as ArrayList<String>

        val application = Application(
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
            null
        )

        db.collection("applications").document(id)
            .set(application)
            .addOnSuccessListener {
                binding.titleEdit.text = null
                binding.companyEdit.text = null
                binding.industryEdit.text = null
                binding.salaryEdit.text = null
                binding.requirementsLevelEdit.text = null
                binding.requirementsExperienceEdit.text = null
                binding.requirementsLocationEdit.text = null
                binding.requirementsEdit.text = null
                binding.descriptionEdit.text = null
                binding.tagsEdit.text = null
                binding.postImage.setImageURI(null)
            }
    }

    private fun setupFabTransition() {
        enterTransition = MaterialContainerTransform().apply {
            startView = requireActivity().findViewById(R.id.fab_add_offer)
            endView = binding.createCardView
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
        var arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, industries)
        binding.industryEdit.setAdapter(arrayAdapter)

        val levels = resources.getStringArray(R.array.levels)
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, levels)
        binding.requirementsLevelEdit.setAdapter(arrayAdapter)

        val tags = resources.getStringArray(R.array.tags).sorted()
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, tags)
        binding.tagsEdit.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        binding.tagsEdit.threshold = 1
        binding.tagsEdit.setAdapter(arrayAdapter)
    }

    private fun getArrayFromString(string: String): List<String> {
        return string.split(",")
    }
}
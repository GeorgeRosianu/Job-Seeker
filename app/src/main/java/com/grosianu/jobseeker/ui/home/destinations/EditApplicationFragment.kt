package com.grosianu.jobseeker.ui.home.destinations

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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentEditApplicationBinding
import com.grosianu.jobseeker.models.Application
import java.util.*

class EditApplicationFragment : Fragment() {

    private var _binding: FragmentEditApplicationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditApplicationViewModel by viewModels()
    private val args: EditApplicationFragmentArgs by navArgs()

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private lateinit var imageUri: Uri
    private var imageFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = uri
                binding.addImageBtn.setImageURI(uri)
                binding.addImageTextView.visibility = View.GONE
                binding.addImageIcon.visibility = View.GONE
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditApplicationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupArrays()

        viewModel.getPost(args.postId)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.addBtn.setOnClickListener {
            if (binding.titleEdit.text.isNullOrEmpty() ||
                binding.companyEdit.text.isNullOrEmpty() ||
                binding.descriptionEdit.text.isNullOrEmpty()
            ) {

                Toast.makeText(
                    requireContext(),
                    "Please fill in the required fields.",
                    Toast.LENGTH_SHORT
                )
                    .show()
                return@setOnClickListener
            }

            updateData()
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

    private fun updateData() {
        val storageReference = storage.getReference("images/${viewModel.post.value?.image}")
        storageReference.putFile(imageUri)
            .addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {
                    updateDataToFirestore(it.toString())
                }
            }
            .addOnFailureListener {
            }
    }

    private fun updateDataToFirestore(image: String) {
        val id: String = viewModel.post.value?.id!!
        val owner: String = viewModel.post.value?.owner!!
        val title: String = binding.titleEdit.text.toString()
        val company: String = binding.companyEdit.text.toString()
        val industry: String = binding.industryEdit.text.toString()
        val salary: Double = binding.salaryEdit.text.toString().toDouble()
        val level: String = binding.requirementsLevelEdit.text.toString()
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
            viewModel.post.value!!.applicants
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
                binding.addImageBtn.setImageURI(null)
                binding.addImageIcon.setImageResource(R.drawable.ic_round_add_24)
                _binding = null
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
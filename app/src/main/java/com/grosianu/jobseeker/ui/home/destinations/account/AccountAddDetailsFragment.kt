package com.grosianu.jobseeker.ui.home.destinations.account

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.util.Pair
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import coil.load
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.transition.MaterialContainerTransform
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentAccountAddDetailsBinding
import com.grosianu.jobseeker.ui.home.destinations.account.viewModels.AccountAddDetailsViewModel
import com.grosianu.jobseeker.ui.home.destinations.account.viewModels.AccountViewModel
import com.grosianu.jobseeker.utils.themeColor
import java.util.*


class AccountAddDetailsFragment : Fragment() {

    private var _binding: FragmentAccountAddDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AccountAddDetailsViewModel by viewModels()

    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private lateinit var imageUri: Uri
    private var imageFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = uri
                binding.imageView.setImageURI(uri)
                binding.imageView.setPadding(0)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountAddDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialization()
    }

    private fun initialization() {
        setupArrays()
        setupTransition()
        setupViews()
    }

    private fun setupViews() {
        viewModel.getUserData()
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.changeProfilePicTextView.setOnClickListener {
            getImageFromGallery()
        }
        binding.dateBtn.setOnClickListener {
            showDatePicker(binding.dateEdit)
        }
        binding.experienceDateBtn.setOnClickListener {
            showDatePicker(binding.experienceDateEdit)
        }
        binding.addBtn.setOnClickListener {
            AccountViewModel().getUserData()
            updateFirestoreData()
        }
        binding.navigationIcon.setOnClickListener {
            findNavController().navigateUp()
        }

        if(viewModel.user.value?.imageUri.isNullOrEmpty()) {
            binding.imageView.setImageResource(R.drawable.ic_broken_image)
            binding.imageView.setPadding(32)
        } else {
            binding.imageView.load(imageUri)
            binding.imageView.setPadding(0)
        }
    }

    private fun getImageFromGallery() = imageFromGallery.launch("image/*")

    private fun updateFirestoreData() {
        if (this::imageUri.isInitialized) {
            val fileName = UUID.randomUUID().toString()
            val storageReference = storage.getReference("images/$fileName")

            storageReference.putFile(imageUri)
                .addOnSuccessListener {
                    storageReference.downloadUrl.addOnSuccessListener {
                        updateUserFirestoreData(it.toString())
                    }
                }
                .addOnFailureListener {
                }
        } else {
            updateUserFirestoreData("")
        }
    }

    private fun updateUserFirestoreData(image: String) {
        val firstName = binding.firstNameEdit.text.toString()
        val lastName = binding.lastNameEdit.text.toString()
        val phoneNumber = binding.phoneEdit.text.toString()
        val age = binding.ageEdit.text.toString()
        val residence = binding.residenceEdit.text.toString()
        val sex = binding.sexEdit.text.toString()
        val educationLevel = binding.typeEdit.text.toString()
        val educationSpec = binding.specializationEdit.text.toString()
        val educationCity = binding.cityEdit.text.toString()
        val educationInstitution = binding.institutionEdit.text.toString()
        val educationDate = binding.dateEdit.text.toString()
        val experiencePosition = binding.positionEdit.text.toString()
        val experienceCompany = binding.companyEdit.text.toString()
        val experienceCity = binding.experienceCityEdit.text.toString()
        val experienceIndustry = binding.industryEdit.text.toString()
        val experienceYears = binding.experienceDateEdit.text.toString()
        val experienceDescription = binding.descriptionEdit.text.toString()
        val displayName = "$firstName $lastName"

        val dbRef = db.collection("users").document(viewModel.user.value?.userId!!)

        dbRef.update("firstName", firstName)
            .addOnSuccessListener { binding.firstNameEdit.text = null }
        dbRef.update("lastName", lastName)
            .addOnSuccessListener { binding.lastNameEdit.text = null }
        dbRef.update("phoneNumber", phoneNumber)
            .addOnSuccessListener { binding.phoneEdit.text = null }
        if (binding.ageEdit.text.toString().isNotEmpty()) {
            dbRef.update("age", age.toInt())
                .addOnSuccessListener { binding.ageEdit.text = null }
        }
        dbRef.update("residence", residence)
            .addOnSuccessListener { binding.residenceEdit.text = null }
        dbRef.update("sex", sex)
            .addOnSuccessListener { binding.sexEdit.text = null }
        dbRef.update("educationLevel", educationLevel)
            .addOnSuccessListener { binding.typeEdit.text = null }
        dbRef.update("educationSpec", educationSpec)
            .addOnSuccessListener { binding.specializationEdit.text = null }
        dbRef.update("educationCity", educationCity)
            .addOnSuccessListener { binding.cityEdit.text = null }
        dbRef.update("educationInstitution", educationInstitution)
            .addOnSuccessListener { binding.institutionEdit.text = null }
        dbRef.update("educationDate", educationDate)
            .addOnSuccessListener { binding.dateEdit.text = null }
        dbRef.update("experiencePosition", experiencePosition)
            .addOnSuccessListener { binding.positionEdit.text = null }
        dbRef.update("experienceCompany", experienceCompany)
            .addOnSuccessListener { binding.companyEdit.text = null }
        dbRef.update("experienceCity", experienceCity)
            .addOnSuccessListener { binding.experienceCityEdit.text = null }
        dbRef.update("experienceIndustry", experienceIndustry)
            .addOnSuccessListener { binding.industryEdit.text = null }
        dbRef.update("experienceYears", experienceYears)
            .addOnSuccessListener { binding.experienceDateEdit.text = null }
        dbRef.update("experienceDescription", experienceDescription)
            .addOnSuccessListener { binding.descriptionEdit.text = null }
        if(firstName.isNotEmpty()) {
            dbRef.update("displayName", displayName)
        }
        if (image.isNotEmpty()) {
            dbRef.update("imageUri", image)
                .addOnSuccessListener { binding.imageView.setImageResource(R.drawable.ic_broken_image) }
        }

        findNavController().navigateUp()
    }

    private fun setupArrays() {
        val cities = resources.getStringArray(R.array.cities)
        var arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, cities)
        binding.residenceEdit.setAdapter(arrayAdapter)
        binding.cityEdit.setAdapter(arrayAdapter)
        binding.experienceCityEdit.setAdapter(arrayAdapter)

        val sex = resources.getStringArray(R.array.sex)
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, sex)
        binding.sexEdit.setAdapter(arrayAdapter)

        val studies = resources.getStringArray(R.array.education_level)
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, studies)
        binding.typeEdit.setAdapter(arrayAdapter)

        val industries = resources.getStringArray(R.array.industries)
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, industries)
        binding.industryEdit.setAdapter(arrayAdapter)
    }

    private fun showDatePicker(inputEdit: TextInputEditText) {
        val currentMonth = MaterialDatePicker.thisMonthInUtcMilliseconds()
        val currentDay = MaterialDatePicker.todayInUtcMilliseconds()
        val currentDate = Pair(currentMonth, currentDay)

        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setSelection(currentDate)
            .build()

        dateRangePicker.addOnPositiveButtonClickListener {
            if (it.first != null && it.second != null) {
                inputEdit.setText(dateRangePicker.headerText)
            } else {
                Toast.makeText(requireContext(), "Invalid dates", Toast.LENGTH_SHORT).show()
            }
        }
        dateRangePicker.addOnCancelListener {
            it.dismiss()
        }
        dateRangePicker.show(parentFragmentManager, "DatePicker")
    }

    private fun setupTransition() {
        enterTransition = Slide().apply {
            duration = resources.getInteger(R.integer.motion_duration_medium).toLong()
            addTarget(R.id.add_profile_details_card_view)
        }
        returnTransition = Slide().apply {
            duration = resources.getInteger(R.integer.motion_duration_medium).toLong()
            addTarget(R.id.add_profile_details_card_view)
        }
    }
}
package com.grosianu.jobseeker.ui.home.destinations.account

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
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Slide
import coil.load
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentAccountAddDetailsBinding
import com.grosianu.jobseeker.models.User
import com.grosianu.jobseeker.ui.home.HomeActivityViewModel
import com.grosianu.jobseeker.ui.home.destinations.account.viewModels.AccountAddDetailsViewModel

class AccountAddDetailsFragment : Fragment() {

    private var binding: FragmentAccountAddDetailsBinding? = null
    private val viewModel: AccountAddDetailsViewModel by viewModels()
    private val sharedViewModel: HomeActivityViewModel by activityViewModels()

    private lateinit var imageUri: Uri
    private var imageFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = uri
                binding?.apply {
                    imageView.setImageURI(uri)
                    imageView.setPadding(0)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentAccountAddDetailsBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupArrays()
        setupTransition()
        setupViews()
    }

    private fun setupViews() {
        binding?.apply {
            viewModel = sharedViewModel
            lifecycleOwner = viewLifecycleOwner

            changeProfilePicTextView.setOnClickListener {
                getImageFromGallery()
            }
            dateBtn.setOnClickListener {
                showDatePicker(binding!!.dateEdit)
            }
            experienceDateBtn.setOnClickListener {
                showDatePicker(binding!!.experienceDateEdit)
            }
            addBtn.setOnClickListener {
                //AccountViewModel().getUserData()
                updateFirestoreData()
            }
            navigationIcon.setOnClickListener {
                findNavController().navigateUp()
            }

            if(sharedViewModel.currentAccount.value?.imageUri.isNullOrEmpty()) {
                imageView.apply {
                    setImageResource(R.drawable.ic_broken_image)
                    setPadding(32)
                }
                //binding.imageView.setImageResource(R.drawable.ic_broken_image)
                //binding.imageView.setPadding(32)
            } else {
                imageView.apply {
                    load(sharedViewModel.currentAccount.value?.imageUri)
                    setPadding(0)
                }
                //binding.imageView.load(imageUri)
                //binding.imageView.setPadding(0)
            }
        }

        //binding.viewModel = viewModel
        //binding.lifecycleOwner = viewLifecycleOwner

        //binding.changeProfilePicTextView.setOnClickListener {
        //    getImageFromGallery()
        //}
        //binding.dateBtn.setOnClickListener {
        //    showDatePicker(binding.dateEdit)
        //}
        //binding.experienceDateBtn.setOnClickListener {
        //    showDatePicker(binding.experienceDateEdit)
        //}
        //binding.addBtn.setOnClickListener {
        //    AccountViewModel().getUserData()
        //    updateFirestoreData()
        //}
        //binding.navigationIcon.setOnClickListener {
        //    findNavController().navigateUp()
        //}
    }

    private fun getImageFromGallery() = imageFromGallery.launch("image/*")

    private fun updateFirestoreData() {
        val firstName = binding?.firstNameEdit?.text.toString()
        val lastName = binding?.lastNameEdit?.text.toString()

        val imageString = if (this::imageUri.isInitialized) {
            imageUri.toString()
        } else {
            ""
        }

        val user = User(
            firstName = firstName,
            lastName = lastName,
            phoneNumber = binding?.phoneEdit?.text.toString(),
            age = binding?.ageEdit?.text.toString().toInt(),
            residence = binding?.residenceEdit?.text.toString(),
            sex = binding?.sexEdit?.text.toString(),
            educationLevel = binding?.typeEdit?.text.toString(),
            educationSpec = binding?.specializationEdit?.text.toString(),
            educationCity = binding?.cityEdit?.text.toString(),
            educationInstitution = binding?.institutionEdit?.text.toString(),
            educationDate = binding?.dateEdit?.text.toString(),
            experiencePosition = binding?.positionEdit?.text.toString(),
            experienceCompany = binding?.companyEdit?.text.toString(),
            experienceCity = binding?.experienceCityEdit?.text.toString(),
            experienceIndustry = binding?.industryEdit?.text.toString(),
            experienceYears = binding?.experienceDateEdit?.text.toString(),
            experienceDescription = binding?.descriptionEdit?.text.toString(),
            imageUri = imageString,
        )

        viewModel.updateFirestoreData(user)
        findNavController().navigateUp()
    }

    private fun updateFields() {
        binding?.firstNameEdit?.text = null
        binding?.lastNameEdit?.text = null
        binding?.phoneEdit?.text = null
        binding?.ageEdit?.text = null
        binding?.residenceEdit?.text = null
        binding?.sexEdit?.text = null
        binding?.typeEdit?.text = null
        binding?.specializationEdit?.text = null
        binding?.cityEdit?.text = null
        binding?.institutionEdit?.text = null
        binding?.dateEdit?.text = null
        binding?.positionEdit?.text = null
        binding?.companyEdit?.text = null
        binding?.companyEdit?.text = null
        binding?.experienceCityEdit?.text = null
        binding?.industryEdit?.text = null
        binding?.experienceDateEdit?.text = null
        binding?.descriptionEdit?.text = null
        binding?.imageView?.setImageResource(R.drawable.ic_broken_image)
    }

    private fun setupArrays() {
        val cities = resources.getStringArray(R.array.cities)
        var arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, cities)
        binding?.residenceEdit?.setAdapter(arrayAdapter)
        binding?.cityEdit?.setAdapter(arrayAdapter)
        binding?.experienceCityEdit?.setAdapter(arrayAdapter)

        val sex = resources.getStringArray(R.array.sex)
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, sex)
        binding?.sexEdit?.setAdapter(arrayAdapter)

        val studies = resources.getStringArray(R.array.education_level)
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, studies)
        binding?.typeEdit?.setAdapter(arrayAdapter)

        val industries = resources.getStringArray(R.array.industries)
        arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown, industries)
        binding?.industryEdit?.setAdapter(arrayAdapter)
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
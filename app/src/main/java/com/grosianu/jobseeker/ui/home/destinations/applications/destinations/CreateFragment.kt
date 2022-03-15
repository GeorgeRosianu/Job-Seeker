package com.grosianu.jobseeker.ui.home.destinations.applications.destinations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.transition.MaterialElevationScale
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.grosianu.jobseeker.R
import com.grosianu.jobseeker.databinding.FragmentCreateBinding
import com.grosianu.jobseeker.models.Application


class CreateFragment: Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!

    private val db = FirebaseFirestore.getInstance()

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

        navigateToCreate()

        binding.addBtn.setOnClickListener(View.OnClickListener {
            val owner: String = FirebaseAuth.getInstance().currentUser?.uid.toString()
            val title: String = binding.titleEdit.text.toString()
            val company: String = binding.companyEdit.text.toString()
            val industry: String = binding.industryEdit.text.toString()
            val salary: Double = binding.salaryEdit.text.toString().toDouble()
            val location: String = binding.locationEdit.text.toString()
            val requirements: String = binding.requirementsEdit.text.toString()
            val description: String = binding.descriptionEdit.text.toString()

            if (title.isEmpty()) {
                Toast.makeText(requireContext(), "Please add title.", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }

            val application = Application(
                owner,
                title,
                company,
                industry,
                salary,
                location,
                requirements,
                description
            )

            db.collection("applications")
                .add(application)
                .addOnSuccessListener(OnSuccessListener<DocumentReference?> {
                    binding.titleEdit.text = null
                    binding.companyEdit.text = null
                    binding.industryEdit.text = null
                    binding.salaryEdit.text = null
                    binding.locationEdit.text = null
                    binding.requirementsEdit.text = null
                    binding.descriptionEdit.text = null
                })
        })
    }

    private fun navigateToCreate() {
        this.apply {
            exitTransition = MaterialElevationScale(false).apply {
                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
            }
        }
    }
}
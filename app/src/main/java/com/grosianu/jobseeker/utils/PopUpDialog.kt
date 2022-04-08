package com.grosianu.jobseeker.utils

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.grosianu.jobseeker.R

class PopUpDialog(array: Array<String>) : DialogFragment() {

    private val dataArray = array

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { it ->
            val checkedItems = ArrayList<Int>()
            val alertBuilder = AlertDialog.Builder(it)

            alertBuilder.setTitle("Select an option")
            alertBuilder.setMultiChoiceItems(dataArray, null) { _, index, checked ->
                if (checked) {
                    checkedItems.add(index)
                } else if (checkedItems.contains(index)) {
                    checkedItems.remove(index)
                }
            }
            alertBuilder.setPositiveButton("OK") { _, _ ->
                val resultArray = ArrayList<String>()
                checkedItems.forEach { index ->
                    resultArray.add(dataArray[index])
                }
                setFragmentResult("requestKey", bundleOf("bundleKey" to resultArray))
            }
            alertBuilder.create()
        } ?: throw IllegalStateException("Exception !! Activity is null !!")
    }
}
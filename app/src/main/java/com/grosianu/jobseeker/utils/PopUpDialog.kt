package com.grosianu.jobseeker.utils

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.grosianu.jobseeker.R

class PopUpDialog(array: Array<String>) : DialogFragment() {

    private val dataArray = array

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val checkedItems = ArrayList<Int>()
            val alertBuilder = AlertDialog.Builder(it)

            alertBuilder.setTitle("Select an option")
            alertBuilder.setMultiChoiceItems(dataArray, null) { dialog, index, checked ->
                if (checked) {
                    checkedItems.add(index)
                } else if (checkedItems.contains(index)) {
                    checkedItems.remove(index)
                }
            }
            alertBuilder.setPositiveButton("OK") { dialog, id ->
                Log.d(
                    "DialogLog",
                    "Ok pressed with checked items : $checkedItems"
                )
            }
            alertBuilder.create()
        } ?: throw IllegalStateException("Exception !! Activity is null !!")
    }
}
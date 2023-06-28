package com.example.eworkout.report.view

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.eworkout.databinding.UpdateBmiDialogBinding
import com.example.eworkout.report.`interface`.UpdateBMIDialogOnClick
import com.example.eworkout.report.util.MathRounder

class UpdateBMIDialog(
    val listener: UpdateBMIDialogOnClick,
    val currentWeight: Double,
    val currentHeight: Double
) : DialogFragment() {
    private var _binding: UpdateBmiDialogBinding? = null
    val binding get() = _binding!!

    var wantToCloseDialog = false;

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            _binding = UpdateBmiDialogBinding.inflate(layoutInflater)

            getCurrentValue()
            // Use the Builder class for convenient dialog construction
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Update BMI")
                .setView(binding.root)
                .setPositiveButton("Save",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
                .setNegativeButton("Cancel",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    @Override
    override fun onResume()
    {
        super.onResume();
        val d = dialog as AlertDialog
        val positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener()
        {
            //Do stuff, possibly set wantToCloseDialog to true then...
            isInvalidText()
            if (wantToCloseDialog)
            {
                val weight = binding.editTextWEIGHT.text.toString().toDouble()
                val height = binding.editTextHEIGHT.text.toString().toDouble()
                listener.onClick(weight, height)
                d.dismiss()
            }
            //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
        }
    }

    private fun isInvalidText(): Boolean
    {
        wantToCloseDialog = true
        if(binding.editTextWEIGHT.text.isEmpty()){
            binding.editTextWEIGHT.setError("Empty field!")
            wantToCloseDialog = false
        }
        if(binding.editTextHEIGHT.text.isEmpty())
        {
            binding.editTextHEIGHT.setError("Empty field!")
            wantToCloseDialog = false
        }
        return wantToCloseDialog
    }

    private fun getCurrentValue()
    {
        binding.editTextWEIGHT.setText(MathRounder.round(currentWeight).toString())
        binding.editTextHEIGHT.setText(MathRounder.round(currentHeight).toString())
    }
}
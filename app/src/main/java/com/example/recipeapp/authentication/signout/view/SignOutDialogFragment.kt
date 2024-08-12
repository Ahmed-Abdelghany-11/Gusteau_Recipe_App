package com.example.recipeapp.authentication.signout.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.recipeapp.R
import com.example.recipeapp.recipe.common.OnSignOutClickListener


class SignOutDialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view =
            requireActivity().layoutInflater.inflate(R.layout.fragment_sign_out_dialog, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(view)
            .setCancelable(false)

        val dialog = builder.create()

        // Set transparent background to remove default dialog styling
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val buttonCancel: Button = view.findViewById(R.id.cancel)
        val buttonConfirm: Button = view.findViewById(R.id.delete)

        buttonCancel.setOnClickListener {
            dismiss()
        }

        buttonConfirm.setOnClickListener {
            (activity as? OnSignOutClickListener)?.signOut()
            dismiss()
        }

        return dialog
    }
    }


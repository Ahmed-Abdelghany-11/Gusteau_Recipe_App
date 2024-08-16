package com.example.recipeapp.recipe.modeDialog.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.recipeapp.R
import com.example.recipeapp.recipe.modeDialog.viewModel.ModeViewModel
import com.google.android.material.switchmaterial.SwitchMaterial

class ModeFragment : DialogFragment() {

    private val themeViewModel: ModeViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view =
            requireActivity().layoutInflater.inflate(R.layout.fragment_mode, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(view)
            .setCancelable(false)

        val dialog = builder.create()

        // Set transparent background to remove default dialog styling
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val modeSwitch = view.findViewById<SwitchMaterial>(R.id.modeSwitch)

        // Observe the isDarkMode LiveData from ViewModel
        themeViewModel.isDarkMode.observe(this) { isDarkMode ->
            modeSwitch.isChecked = isDarkMode
        }

        val sharedPreferences = requireActivity().getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        modeSwitch.setOnCheckedChangeListener { _, isChecked ->
            editor.putBoolean("DarkMode", isChecked)
            editor.apply()

            themeViewModel.setDarkMode(isChecked)
        }

        return dialog
    }

}
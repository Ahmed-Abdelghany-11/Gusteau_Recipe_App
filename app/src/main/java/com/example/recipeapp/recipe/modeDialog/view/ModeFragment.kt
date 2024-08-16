package com.example.recipeapp.recipe.modeDialog.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.R
import com.example.recipeapp.authentication.AuthActivity
import com.example.recipeapp.data.local.LocalDataSourceImpl
import com.example.recipeapp.data.remote.APIClient
import com.example.recipeapp.data.sharedPreference.SettingSharedPref
import com.example.recipeapp.recipe.RecipeActivity
import com.example.recipeapp.recipe.category.repo.CategoryRepoImp
import com.example.recipeapp.recipe.category.viewModel.CatViewModelFactory
import com.example.recipeapp.recipe.category.viewModel.CategoryViewModel
import com.example.recipeapp.recipe.modeDialog.repo.ModeRepoImp
import com.example.recipeapp.recipe.modeDialog.viewModel.ModeViewModel
import com.example.recipeapp.recipe.modeDialog.viewModel.ModeViewModelFactory
import com.google.android.material.switchmaterial.SwitchMaterial

class ModeFragment : DialogFragment() {

    private lateinit var themeViewModel: ModeViewModel

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
        gettingViewModelReady()
         themeViewModel.initializeDarkMode()
        modeSwitch.setOnCheckedChangeListener { _, isChecked ->
            themeViewModel.setDarkMode(isChecked)
            (requireActivity() as? RecipeActivity)?.applyTheme(isChecked)
        }


        themeViewModel.isDarkMode.observe(this) { isDarkMode ->
            modeSwitch.isChecked = isDarkMode
        }


        return dialog
    }

    private fun gettingViewModelReady() {
        val modeViewModelFactory = ModeViewModelFactory(
            ModeRepoImp(
                settingSharedPref = SettingSharedPref(requireContext())
            )
        )
        themeViewModel = ViewModelProvider(this, modeViewModelFactory)[ModeViewModel::class.java]
    }

}
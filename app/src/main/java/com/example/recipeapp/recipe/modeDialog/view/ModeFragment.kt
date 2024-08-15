package com.example.recipeapp.recipe.modeDialog.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.example.recipeapp.R
import com.example.recipeapp.recipe.common.OnDeleteMealListener
import com.example.recipeapp.recipe.favorite.view.FavouritesFragment
import com.example.recipeapp.recipe.favorite.view.adapter.OnDeleteFavMealListener

class ModeFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val view =
            requireActivity().layoutInflater.inflate(R.layout.fragment_mode, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(view)
            .setCancelable(false)

        val dialog = builder.create()

        // Set transparent background to remove default dialog styling
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog
    }

}
package com.example.recipeapp.deleteMealDialog.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.navArgs
import com.example.recipeapp.R
import com.example.recipeapp.common.OnDeleteMealListener
import com.example.recipeapp.favorite.view.FavouritesFragment
import com.example.recipeapp.favorite.view.adapter.OnDeleteFavMealListener

class DeleteFavDialogFragment : DialogFragment() {

    private val args: DeleteFavDialogFragmentArgs by navArgs()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val meal = args.meal

        val view =
            requireActivity().layoutInflater.inflate(R.layout.fragment_delete_fav_dialog, null)
        val builder = AlertDialog.Builder(requireContext())
            .setView(view)
            .setCancelable(false)

        val dialog = builder.create()

        // Set transparent background to remove default dialog styling
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)


        val buttonCancel: Button = view.findViewById(R.id.cancel)
        val buttonConfirm: Button = view.findViewById(R.id.delete)

        buttonCancel.setOnClickListener {
            if (parentFragment is FavouritesFragment) {
                (parentFragment as? OnDeleteFavMealListener)?.cancel(meal)
            }
            dismiss()
        }

        buttonConfirm.setOnClickListener {
            if (parentFragment is FavouritesFragment)
                (parentFragment as? OnDeleteFavMealListener)?.confirmDelete(meal)
            else
                (parentFragment as? OnDeleteMealListener)?.confirmDelete(meal)
            dismiss()
        }

        return dialog
    }


}






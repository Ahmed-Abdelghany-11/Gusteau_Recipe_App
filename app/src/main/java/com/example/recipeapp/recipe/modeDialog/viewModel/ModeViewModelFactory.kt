package com.example.recipeapp.recipe.modeDialog.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.recipe.category.viewModel.CategoryViewModel
import com.example.recipeapp.recipe.modeDialog.repo.ModeRepo

class ModeViewModelFactory(
    private val modeRepo: ModeRepo
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ModeViewModel::class.java)) {
            return ModeViewModel(modeRepo) as T
        }
        throw IllegalArgumentException("Unknown ModeViewModel class")
    }
}
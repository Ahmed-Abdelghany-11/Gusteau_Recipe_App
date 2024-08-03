package com.example.recipeapp.category.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.category.repo.CategoryRepo
import com.example.recipeapp.category.repo.CategoryRepoImp
import com.example.recipeapp.favorite.viewmodel.FavViewModel

class CatViewModelFactory(
    private val categoryRepo: CategoryRepo
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoryViewModel::class.java)) {
            return CategoryViewModel(categoryRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
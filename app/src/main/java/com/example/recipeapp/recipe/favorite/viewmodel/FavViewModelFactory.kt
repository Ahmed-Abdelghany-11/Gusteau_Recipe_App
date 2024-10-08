package com.example.recipeapp.recipe.favorite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.recipe.favorite.repo.FavRepo

class FavViewModelFactory(private val repo: FavRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavViewModel::class.java)) {
            return FavViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}
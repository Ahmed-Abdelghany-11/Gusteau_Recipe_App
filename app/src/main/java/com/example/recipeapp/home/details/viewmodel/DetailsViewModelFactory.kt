package com.example.recipeapp.home.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.home.details.repo.DetailsRepo

class DetailsViewModelFactory (private val repo : DetailsRepo) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DetailsViewModel::class.java))
            DetailsViewModel(repo) as T
        else throw IllegalArgumentException("DetailsViewModel class not found")
    }
}
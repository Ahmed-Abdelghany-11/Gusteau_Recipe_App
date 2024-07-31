package com.example.recipeapp.home.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.recipeapp.home.search.repo.SearchRepository

class SearchViewModelFactory(
    private val searchRepository: SearchRepository
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SearchViewModel::class.java))
            SearchViewModel(searchRepository) as T
        else throw IllegalArgumentException("SearchViewModel class not found")
    }
}
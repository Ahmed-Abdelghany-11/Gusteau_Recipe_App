package com.example.recipeapp.home.details.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.home.details.repo.DetailsRepo
import kotlinx.coroutines.launch

class DetailsViewModel(private val repo : DetailsRepo) :ViewModel(){

    fun insertIntoFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch {
            repo.insertIntoFav(userMealCrossRef)
        }
    }
    fun insertMeal(meal: Meal)=
        viewModelScope.launch {
            repo.insertMeal(meal)
        }

    fun deleteMeal(meal: Meal)=
        viewModelScope.launch {
            repo.deleteMeal(meal)
        }
}
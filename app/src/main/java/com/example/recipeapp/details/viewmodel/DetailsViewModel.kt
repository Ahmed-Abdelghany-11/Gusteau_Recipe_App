package com.example.recipeapp.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.details.repo.DetailsRepo
import kotlinx.coroutines.launch

class DetailsViewModel(private val repo : DetailsRepo) :ViewModel(){

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _meal = MutableLiveData<MealList>()
    val meal: LiveData<MealList> get() = _meal

    fun insertIntoFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch {
            repo.insertIntoFav(userMealCrossRef)
        }
    }

    fun deleteFromFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch {
            repo.deleteFromFav(userMealCrossRef)
        }
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            repo.insertMeal(meal)
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            repo.deleteMeal(meal)
        }
    }

     fun isFavoriteMeal(userId: Int, mealId: String)  {
         viewModelScope.launch {
             val isFavorite = repo.isFavoriteMeal(userId, mealId)
             _isFavorite.postValue(isFavorite)
         }
    }

    fun getMealById(userId: String) {
        viewModelScope.launch {
            _meal.postValue(repo.getMealById(userId))
        }
    }
}
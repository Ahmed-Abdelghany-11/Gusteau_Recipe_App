package com.example.recipeapp.recipe.details.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.recipe.details.repo.DetailsRepo
import kotlinx.coroutines.launch

class DetailsViewModel(private val repo: DetailsRepo) : ViewModel() {

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _meal = MutableLiveData<MealList>()
    val meal: LiveData<MealList> get() = _meal

    fun insertIntoFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch {
            try {
                repo.insertIntoFav(userMealCrossRef)
            } catch (e: Exception) {
                Log.e("DetailsViewModel", "Error inserting into favorites: ${e.message}")
            }
        }
    }

    fun deleteFromFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch {
            try {
                repo.deleteFromFav(userMealCrossRef)
            } catch (e: Exception) {
                Log.e("DetailsViewModel", "Error deleting from favorites: ${e.message}")
            }
        }
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            try {
                repo.insertMeal(meal)
            } catch (e: Exception) {
                Log.e("DetailsViewModel", "Error inserting meal: ${e.message}")
            }
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            try {
                repo.deleteMeal(meal)
            } catch (e: Exception) {
                Log.e("DetailsViewModel", "Error deleting meal: ${e.message}")
            }
        }
    }

    fun isFavoriteMeal(userId: Int, mealId: String) {
        viewModelScope.launch {
            try {
                val isFavorite = repo.isFavoriteMeal(userId, mealId)
                _isFavorite.postValue(isFavorite)
            } catch (e: Exception) {
                Log.e("DetailsViewModel", "Error checking if meal is favorite: ${e.message}")
            }
        }
    }

    fun getMealById(userId: String) {
        viewModelScope.launch {
            try {
                _meal.postValue(repo.getMealById(userId))
            } catch (e: Exception) {
                Log.e("DetailsViewModel", "Error fetching meal by ID: ${e.message}")
            }
        }
    }
}

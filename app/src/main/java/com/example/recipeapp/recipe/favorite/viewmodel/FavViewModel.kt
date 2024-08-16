package com.example.recipeapp.recipe.favorite.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.local.model.UserWithMeal
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.recipe.favorite.repo.FavRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavViewModel(private val repo: FavRepo) : ViewModel() {

    private val _userFavMeals = MutableLiveData<UserWithMeal?>()
    val userFavMeals: LiveData<UserWithMeal?> get() = _userFavMeals

    fun getUserWithMeals(userId: Int) {
        viewModelScope.launch {
            try {
                _userFavMeals.postValue(repo.getUserWithMeals(userId))
            } catch (e: Exception) {
                Log.e("FavViewModel", "Error fetching user favorite meals: ${e.message}")
            }
        }
    }

    fun deleteFromFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repo.deleteFromFav(userMealCrossRef)
            } catch (e: Exception) {
                Log.e("FavViewModel", "Error deleting from favorites: ${e.message}")
            }
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            try {
                repo.deleteMeal(meal)
            } catch (e: Exception) {
                Log.e("FavViewModel", "Error deleting meal: ${e.message}")
            }
        }
    }
}

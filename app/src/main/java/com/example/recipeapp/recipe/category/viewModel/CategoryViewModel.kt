package com.example.recipeapp.recipe.category.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.recipe.category.repo.CategoryRepo
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryRepo: CategoryRepo,
) : ViewModel() {

    private val _categoryRecipes = MutableLiveData<MealList>()
    val categoryRecipes: LiveData<MealList>
        get() = _categoryRecipes

    private val _isFavoriteMeal = MutableLiveData<Boolean>()
    val isFavoriteMeal: LiveData<Boolean>
        get() = _isFavoriteMeal

    fun getRecipesOfCategory(categoryName: String) {
        viewModelScope.launch {
            try {
                _categoryRecipes.postValue(categoryRepo.getRecipesOfCategory(categoryName))
            } catch (e: Exception) {
                Log.e("CategoryViewModel", "Error fetching recipes: ${e.message}")
            }
        }
    }

    fun insertIntoFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch {
            try {
                categoryRepo.insertIntoFav(userMealCrossRef)
            } catch (e: Exception) {
                Log.e("CategoryViewModel", "Error inserting into favorites: ${e.message}")
            }
        }
    }

    fun deleteFromFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch {
            try {
                categoryRepo.deleteFromFav(userMealCrossRef)
            } catch (e: Exception) {
                Log.e("CategoryViewModel", "Error deleting from favorites: ${e.message}")
            }
        }
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            try {
                categoryRepo.insertMeal(meal)
            } catch (e: Exception) {
                Log.e("CategoryViewModel", "Error inserting meal: ${e.message}")
            }
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            try {
                categoryRepo.deleteMeal(meal)
            } catch (e: Exception) {
                Log.e("CategoryViewModel", "Error deleting meal: ${e.message}")
            }
        }
    }

    fun isFavoriteMeal(userId: Int, mealId: String): MutableLiveData<Boolean> {
        val isFav = MutableLiveData<Boolean>()
        viewModelScope.launch {
            try {
                isFav.postValue(categoryRepo.isFavoriteMeal(userId, mealId))
            } catch (e: Exception) {
                Log.e("CategoryViewModel", "Error checking if meal is favorite: ${e.message}")
            }
        }
        return isFav
    }
}

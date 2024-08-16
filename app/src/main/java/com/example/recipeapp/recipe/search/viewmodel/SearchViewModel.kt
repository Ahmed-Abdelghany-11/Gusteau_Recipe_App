package com.example.recipeapp.recipe.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.recipe.search.repo.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _searchResultOfMeals = MutableLiveData<MealList?>()
    val searchResultOfMeals: LiveData<MealList?>
        get() = _searchResultOfMeals

    private val _isFavMeal = MutableLiveData<Boolean>()
    val isFavMeal: LiveData<Boolean>
        get() = _isFavMeal

    fun getSearchResult(name: String) {
        viewModelScope.launch {
            try {
                val result = searchRepository.getMealByName(name) ?: MealList(emptyList())
                _searchResultOfMeals.postValue(result)
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error fetching search results: ${e.message}")
            }
        }
    }

    fun insertIntoFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch {
            try {
                searchRepository.insertIntoFav(userMealCrossRef)
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error inserting into favorites: ${e.message}")
            }
        }
    }

    fun deleteFromFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch {
            try {
                searchRepository.deleteFromFav(userMealCrossRef)
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error deleting from favorites: ${e.message}")
            }
        }
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            try {
                searchRepository.insertMeal(meal)
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error inserting meal: ${e.message}")
            }
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            try {
                searchRepository.deleteMeal(meal)
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error deleting meal: ${e.message}")
            }
        }
    }

    fun isFavoriteMeal(userId: Int, mealId: String): LiveData<Boolean> {
        val isFavorite = MutableLiveData<Boolean>()
        viewModelScope.launch {
            try {
                isFavorite.postValue(searchRepository.isFavoriteMeal(userId, mealId))
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error checking if meal is favorite: ${e.message}")
            }
        }
        return isFavorite
    }
}

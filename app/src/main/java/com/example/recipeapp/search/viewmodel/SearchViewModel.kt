package com.example.recipeapp.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.search.repo.SearchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
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

    fun getSearchResult(name: String) =
        try {
            viewModelScope.launch {
                val result = searchRepository.getMealByName(name) ?: MealList(emptyList())
                _searchResultOfMeals.postValue(result)
            }
        }catch (e: Exception) {
            Log.d("Exception", e.printStackTrace().toString())
        }

    fun insertIntoFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch {
            searchRepository.insertIntoFav(userMealCrossRef)
        }
    }

    fun deleteFromFav(userMealCrossRef: UserMealCrossRef) =
        viewModelScope.launch {
            searchRepository.deleteFromFav(userMealCrossRef)
        }

    fun insertMeal(meal: Meal) =
        viewModelScope.launch {
            searchRepository.insertMeal(meal)
        }

    fun deleteMeal(meal: Meal) =
        viewModelScope.launch {
            searchRepository.deleteMeal(meal)
        }

    fun isFavoriteMeal(userId: Int, mealId: String): LiveData<Boolean> {
        val isFavorite = MutableLiveData<Boolean>()
        viewModelScope.launch {
            isFavorite.postValue(searchRepository.isFavoriteMeal(userId, mealId))
        }
        return isFavorite
    }

}


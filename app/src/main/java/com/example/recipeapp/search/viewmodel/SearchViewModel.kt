package com.example.recipeapp.search.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.search.repo.SearchRepository
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
        viewModelScope.launch {
            val result = searchRepository.getMealByName(name) ?: MealList(emptyList())
            _searchResultOfMeals.postValue(result)
        }

    fun insertIntoFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch {
            searchRepository.insertIntoFav(userMealCrossRef)
        }
    }

    fun deleteFromFav(userMealCrossRef: UserMealCrossRef)=
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

    fun isFavouriteMeal(userId: Int, mealId: String) =
        viewModelScope.launch {
            _isFavMeal.postValue(searchRepository.isFavoriteMeal(userId, mealId))
        }
}
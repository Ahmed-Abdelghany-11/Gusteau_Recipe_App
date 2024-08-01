package com.example.recipeapp.home.favorite.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.local.model.UserWithMeal
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.home.favorite.repo.FavRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch

class FavViewModel (private val repo: FavRepo) : ViewModel() {

    private val _userFavMeals = MutableLiveData<UserWithMeal?>()
    val userFavMeals: LiveData<UserWithMeal?> get() = _userFavMeals


    fun gerUserWithMeals(userId:Int){
        viewModelScope.launch {
            _userFavMeals.postValue(repo.getUserWithMeals(userId))
        }
    }


    fun deleteFromFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFromFav(userMealCrossRef)
        }
    }

    fun deleteMeal(meal: Meal)=
        viewModelScope.launch {
            repo.deleteMeal(meal)
        }


}
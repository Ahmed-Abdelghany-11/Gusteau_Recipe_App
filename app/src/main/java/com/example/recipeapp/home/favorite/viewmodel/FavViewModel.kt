package com.example.recipeapp.home.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.local.model.UserWithMeal
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.home.favorite.repo.FavRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavViewModel (private val repo: FavRepo) : ViewModel() {

    private val _userFavMeals = MutableLiveData<List<UserWithMeal>>()
    val userFavMeals: LiveData<List<UserWithMeal>> get() = _userFavMeals

    fun getAllUserFavMeals(userId : Int) {
        viewModelScope.launch {
            _userFavMeals.postValue(repo.getAllUserFavMeals(userId))
        }
    }

    fun deleteFromFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteFromFav(userMealCrossRef)
        }
    }


}
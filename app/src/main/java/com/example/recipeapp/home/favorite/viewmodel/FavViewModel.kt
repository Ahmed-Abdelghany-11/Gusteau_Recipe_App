package com.example.recipeapp.home.favorite.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.local.model.UserWithMeal
import com.example.recipeapp.home.favorite.repo.FavRepo
import kotlinx.coroutines.launch

class FavViewModel (private val repo: FavRepo) : ViewModel() {

    private val _userFavMeals = MutableLiveData<List<UserWithMeal>>()
    val userFavMeals: LiveData<List<UserWithMeal>> get() = _userFavMeals

    fun getAllUserFavMeals() {
        viewModelScope.launch {
            _userFavMeals.postValue(repo.getAllUserFavMeals())
        }
    }


}
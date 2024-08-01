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

    private val _userFavMeals2 = MutableLiveData<List<Meal>>()
    val userFavMeals2: LiveData<List<Meal>> get() = _userFavMeals2

    private val _getMyResponse = MutableLiveData<MealList?>()
    val getMyResponse: LiveData<MealList?> = _getMyResponse

    fun getAllUserFavMeals(userId : Int){
        viewModelScope.launch {
            val favMeals = repo.getAllUserFavMeals(userId)
            Log.d("FavViewModel", "Fetched favorite meals: $favMeals")
            _userFavMeals2.postValue(favMeals)
        }
    }

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

    fun getMealById(mealId: String)  {
        viewModelScope.launch {
             _getMyResponse.postValue(repo.getMealById(mealId))
        }
    }

    fun deleteMeal(meal: Meal)=
        viewModelScope.launch {
            repo.deleteMeal(meal)
        }


}
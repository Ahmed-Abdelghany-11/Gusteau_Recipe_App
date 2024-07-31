package com.example.recipeapp.home.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.RetrofitHelper
import com.example.recipeapp.data.remote.dto.Category
import com.example.recipeapp.data.remote.dto.CategoryList
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.home.home.repo.RetrofitRepoImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel (private val myRepo : RetrofitRepoImp) : ViewModel() {
    private val _getMyResponse = MutableLiveData<MealList?>()
    val getMyResponse: LiveData<MealList?> = _getMyResponse

    private val _getMealsByLetterResponse = MutableLiveData<MealList?>()
    val getMealsByLetterResponse: LiveData<MealList?> = _getMealsByLetterResponse

    private val _getAllCategoriesResponse = MutableLiveData<CategoryList>()
    val getAllCategoriesResponse: LiveData<CategoryList> = _getAllCategoriesResponse

    fun getMyResponse() {
        viewModelScope.launch {
            _getMyResponse.postValue(myRepo.getMyResponse())
        }
    }

    fun getMealsByRandomLetter() {
        viewModelScope.launch {
            val randomLetter = ('a'..'z').random()
            val response = myRepo.getMealByFirstLetter(randomLetter.toString())
            _getMealsByLetterResponse.postValue(response)
        }
    }

    fun getAllCategories() {
        viewModelScope.launch {
            _getAllCategoriesResponse.postValue(myRepo.getAllCategories())
        }
    }

    fun insertIntoFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch {
            myRepo.insertIntoFav(userMealCrossRef)
        }
    }
    fun insertMeal(meal: Meal)=
        viewModelScope.launch {
            myRepo.insertMeal(meal)
        }

    fun deleteMeal(meal: Meal)=
        viewModelScope.launch {
            myRepo.deleteMeal(meal)
        }
}
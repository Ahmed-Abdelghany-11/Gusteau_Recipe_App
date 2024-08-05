package com.example.recipeapp.category.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.category.repo.CategoryRepo
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


    fun getRecipesOfCategory(categoryName: String) =
        try {
            viewModelScope.launch {
                _categoryRecipes.postValue(categoryRepo.getRecipesOfCategory(categoryName))
            }
        }catch (e: Exception) {
            Log.d("Exception", e.printStackTrace().toString())
        }


    fun insertIntoFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch {
            categoryRepo.insertIntoFav(userMealCrossRef)
        }
    }

    fun deleteFromFav(userMealCrossRef: UserMealCrossRef) =
        viewModelScope.launch {
            categoryRepo.deleteFromFav(userMealCrossRef)
        }

    fun insertMeal(meal: Meal) =
        viewModelScope.launch {
            categoryRepo.insertMeal(meal)
        }

    fun deleteMeal(meal: Meal) =
        viewModelScope.launch {
            categoryRepo.deleteMeal(meal)
        }

    fun isFavoriteMeal(userId: Int, mealId: String): MutableLiveData<Boolean> {
        val isFav = MutableLiveData<Boolean>()
        viewModelScope.launch {
            isFav.postValue(categoryRepo.isFavoriteMeal(userId, mealId))
        }
        return isFav
    }


}
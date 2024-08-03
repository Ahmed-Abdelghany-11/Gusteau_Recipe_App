package com.example.recipeapp.category.viewModel

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
    private val categoryRepo: CategoryRepo
): ViewModel() {

    private val _categoryRecipes= MutableLiveData<MealList>()
    val categoryRecipes:LiveData<MealList>
        get()= _categoryRecipes



    fun getRecipesOfCategory(categoryName:String)=
        viewModelScope.launch {
            _categoryRecipes.postValue(categoryRepo.getRecipesOfCategory(categoryName))
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

    fun isFavoriteMeal(userId: Int, mealId: String): LiveData<Boolean> {
        val isFavorite = MutableLiveData<Boolean>()
        viewModelScope.launch {
            isFavorite.postValue(categoryRepo.isFavoriteMeal(userId, mealId))
        }
        return isFavorite
    }

}
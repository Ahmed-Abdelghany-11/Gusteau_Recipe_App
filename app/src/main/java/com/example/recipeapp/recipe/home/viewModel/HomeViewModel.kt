package com.example.recipeapp.recipe.home.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.local.model.UserWithMeal
import com.example.recipeapp.data.remote.dto.CategoryList
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import com.example.recipeapp.recipe.home.repo.RetrofitRepoImp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val myRepo: RetrofitRepoImp) : ViewModel() {
    private val _getMyResponse = MutableLiveData<MealList?>()
    val getMyResponse: LiveData<MealList?> = _getMyResponse

    private val _getMealsByLetterResponse = MutableLiveData<MealList?>()
    val getMealsByLetterResponse: LiveData<MealList?> = _getMealsByLetterResponse

    private val _getAllCategoriesResponse = MutableLiveData<CategoryList>()
    val getAllCategoriesResponse: LiveData<CategoryList> = _getAllCategoriesResponse

    private val _userFavMeals = MutableLiveData<UserWithMeal?>()
    val userFavMeals: LiveData<UserWithMeal?> get() = _userFavMeals

    private val _isFavoriteMeal = MutableLiveData<Boolean>()
    val isFavoriteMeal: LiveData<Boolean> get() = _isFavoriteMeal

    fun getMyResponse() {
        viewModelScope.launch {
            try {
                _getMyResponse.postValue(myRepo.getMyResponse())
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching my response: ${e.message}")
            }
        }
    }

    fun getMealsByRandomLetter() {
        viewModelScope.launch {
            try {
                fetchMealsByRandomLetter()
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching meals by random letter: ${e.message}")
            }
        }
    }

    private suspend fun fetchMealsByRandomLetter() {
        val randomLetter = ('a'..'z').random()
        try {
            val response = myRepo.getMealByFirstLetter(randomLetter.toString())
            if (response?.meals.isNullOrEmpty()) {
                fetchMealsByRandomLetter()
            } else {
                _getMealsByLetterResponse.postValue(response)
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error fetching meals by letter: ${e.message}")
        }
    }

    fun getAllCategories() {
        viewModelScope.launch {
            try {
                _getAllCategoriesResponse.postValue(myRepo.getAllCategories())
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching all categories: ${e.message}")
            }
        }
    }

    fun insertIntoFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch {
            try {
                myRepo.insertIntoFav(userMealCrossRef)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error inserting into favorites: ${e.message}")
            }
        }
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            try {
                myRepo.insertMeal(meal)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error inserting meal: ${e.message}")
            }
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            try {
                myRepo.deleteMeal(meal)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error deleting meal: ${e.message}")
            }
        }
    }

    fun deleteFromFav(userMealCrossRef: UserMealCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                myRepo.deleteFromFav(userMealCrossRef)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error deleting from favorites: ${e.message}")
            }
        }
    }

    fun isFavoriteMeal(userId: Int, mealId: String): LiveData<Boolean> {
        val isFavorite = MutableLiveData<Boolean>()
        viewModelScope.launch {
            try {
                isFavorite.postValue(myRepo.isFavoriteMeal(userId, mealId))
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error checking if meal is favorite: ${e.message}")
            }
        }
        return isFavorite
    }

    fun gerUserWithMeals(userId: Int) {
        viewModelScope.launch {
            try {
                _userFavMeals.postValue(myRepo.getUserWithMeals(userId))
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching user with meals: ${e.message}")
            }
        }
    }
}

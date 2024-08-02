package com.example.recipeapp.home.details.repo

import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal

interface DetailsRepo {
    suspend fun insertIntoFav(userMealCrossRef: UserMealCrossRef)
    suspend fun deleteFromFav(userMealCrossRef: UserMealCrossRef)
    suspend fun insertMeal(meal: Meal)
    suspend fun deleteMeal(meal: Meal)
    suspend fun isFavoriteMeal(userId: Int, mealId: String) : Boolean
}
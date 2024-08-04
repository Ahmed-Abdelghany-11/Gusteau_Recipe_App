package com.example.recipeapp.details.repo

import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList

interface DetailsRepo {
    suspend fun insertIntoFav(userMealCrossRef: UserMealCrossRef)
    suspend fun deleteFromFav(userMealCrossRef: UserMealCrossRef)
    suspend fun insertMeal(meal: Meal)
    suspend fun deleteMeal(meal: Meal)
    suspend fun isFavoriteMeal(userId: Int, mealId: String) : Boolean
    suspend fun getMealById(id: String): MealList
}
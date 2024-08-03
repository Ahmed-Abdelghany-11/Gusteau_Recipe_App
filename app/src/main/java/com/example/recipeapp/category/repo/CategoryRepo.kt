package com.example.recipeapp.category.repo

import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList

interface CategoryRepo {
    suspend fun getRecipesOfCategory(categoryName:String):MealList

    suspend fun insertIntoFav(userMealCrossRef: UserMealCrossRef)

    suspend fun insertMeal(meal: Meal)

    suspend fun deleteMeal(meal: Meal)

    suspend  fun isFavoriteMeal(userId:Int,mealId:String): Boolean

    suspend fun deleteFromFav(userMealCrossRef: UserMealCrossRef)
}
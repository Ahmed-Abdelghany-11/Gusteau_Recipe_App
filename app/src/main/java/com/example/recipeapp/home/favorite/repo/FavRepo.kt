package com.example.recipeapp.home.favorite.repo

import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.local.model.UserWithMeal
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList

interface FavRepo {
    suspend fun getAllUserFavMeals(userId : Int): List<Meal>
    suspend fun deleteFromFav(userWithMeals: UserMealCrossRef)
    suspend fun getMealById(id: String): MealList
}
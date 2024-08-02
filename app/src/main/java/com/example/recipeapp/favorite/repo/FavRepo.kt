package com.example.recipeapp.favorite.repo

import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.local.model.UserWithMeal
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList

interface FavRepo {
    suspend fun getAllUserFavMeals(userId : Int): List<Meal>
    suspend fun deleteFromFav(userWithMeals: UserMealCrossRef)
    suspend fun getMealById(id: String): MealList
    suspend fun getUserWithMeals(userId: Int): UserWithMeal?

    suspend fun insertMeal(meal: Meal)
    suspend fun deleteMeal(meal: Meal)

}
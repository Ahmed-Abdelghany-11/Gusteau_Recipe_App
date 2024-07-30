package com.example.recipeapp.home.favorite.repo

import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.local.model.UserWithMeal
import com.example.recipeapp.data.remote.dto.Meal

interface FavRepo {
    suspend fun getAllUserFavMeals(userId : Int): List<Meal>
    suspend fun deleteFromFav(userWithMeals: UserMealCrossRef)
}
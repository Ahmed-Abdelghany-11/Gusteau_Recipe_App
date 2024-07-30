package com.example.recipeapp.home.favorite.repo

import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.local.model.UserWithMeal

interface FavRepo {
    suspend fun getAllUserFavMeals(userId : Int): List<UserWithMeal>
    suspend fun deleteFromFav(userWithMeals: UserMealCrossRef)
}
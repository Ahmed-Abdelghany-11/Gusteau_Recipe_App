package com.example.recipeapp.home.favorite.repo

import com.example.recipeapp.data.local.model.UserWithMeal

interface FavRepo {
    suspend fun getAllUserFavMeals(): List<UserWithMeal>
}
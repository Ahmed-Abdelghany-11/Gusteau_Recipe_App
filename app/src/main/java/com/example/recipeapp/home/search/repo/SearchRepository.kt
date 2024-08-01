package com.example.recipeapp.home.search.repo

import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.RetrofitHelper
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList

interface SearchRepository {
    suspend fun getMealByName(name: String): MealList?

    suspend fun insertIntoFav(userMealCrossRef: UserMealCrossRef)

    suspend fun insertMeal(meal: Meal)

    suspend fun deleteMeal(meal: Meal)
}
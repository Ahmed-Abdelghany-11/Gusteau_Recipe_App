package com.example.recipeapp.home.home.repo

import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Category
import com.example.recipeapp.data.remote.dto.CategoryList
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList

interface RetrofitRepo {
    suspend fun getMyResponse() : MealList?

    suspend fun getMealByFirstLetter(letter: String) : MealList?

    suspend fun getAllCategories() : CategoryList

    suspend fun insertIntoFav(userMealCrossRef: UserMealCrossRef)

    suspend fun insertMeal(meal: Meal)
    suspend fun deleteMeal(meal: Meal)
}
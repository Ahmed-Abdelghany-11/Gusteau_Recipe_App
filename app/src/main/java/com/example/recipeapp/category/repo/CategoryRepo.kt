package com.example.recipeapp.category.repo

import com.example.recipeapp.data.remote.dto.MealList

interface CategoryRepo {
    suspend fun getRecipesOfCategory(categoryName:String):MealList
}
package com.example.recipeapp.data.remote

import com.example.recipeapp.data.remote.dto.Category
import com.example.recipeapp.data.remote.dto.MealList

interface RemoteDataSource {

    suspend fun getRandomMeal(): MealList

    suspend fun getMealByName(name: String): MealList

    suspend fun getMealByFirstLetter(letter: String): MealList

    suspend fun getAllCategories(): List<Category>


}
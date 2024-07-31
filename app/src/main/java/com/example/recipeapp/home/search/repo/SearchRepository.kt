package com.example.recipeapp.home.search.repo

import com.example.recipeapp.data.remote.RetrofitHelper
import com.example.recipeapp.data.remote.dto.MealList

interface SearchRepository {
    suspend fun getMealByName(name: String): MealList


}
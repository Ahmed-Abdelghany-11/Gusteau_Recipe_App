package com.example.recipeapp.home.home.repo

import com.example.recipeapp.data.remote.dto.MealList

interface RetrofitRepo {
    suspend fun getMyResponse() : MealList
}
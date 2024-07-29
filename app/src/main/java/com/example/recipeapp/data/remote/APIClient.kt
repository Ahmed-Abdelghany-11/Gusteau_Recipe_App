package com.example.recipeapp.data.remote

import com.example.recipeapp.data.remote.dto.Category
import com.example.recipeapp.data.remote.dto.MealList

object APIClient : RemoteDataSource {
    override suspend fun getRandomMeal(): MealList {
        return RetrofitHelper.service.getRandomMeal()
    }

    override suspend fun getMealByName(name: String): MealList {
        return RetrofitHelper.service.getMealByName(name)
    }

    override suspend fun getMealByFirstLetter(letter: String): MealList {
        return RetrofitHelper.service.getMealByFirstLetter(letter)
    }

    override suspend fun getAllCategories(): List<Category> {
        return RetrofitHelper.service.getAllCategories()
    }

}
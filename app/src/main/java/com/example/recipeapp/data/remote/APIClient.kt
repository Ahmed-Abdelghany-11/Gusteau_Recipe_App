package com.example.recipeapp.data.remote

import com.example.recipeapp.data.remote.dto.Category
import com.example.recipeapp.data.remote.dto.CategoryList
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

    override suspend fun getAllCategories(): CategoryList {
        return RetrofitHelper.service.getAllCategories()
    }

    override suspend fun getMealById(id: String): MealList {
        return RetrofitHelper.service.getMealById(id)
    }

    override suspend fun getRecipesOfCategory(categoryName: String): MealList {
        return RetrofitHelper.service.getRecipesOfCategory(categoryName)
    }

}
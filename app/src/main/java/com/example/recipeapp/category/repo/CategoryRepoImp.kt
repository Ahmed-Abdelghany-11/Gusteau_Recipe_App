package com.example.recipeapp.category.repo

import com.example.recipeapp.data.remote.RemoteDataSource
import com.example.recipeapp.data.remote.dto.MealList

class CategoryRepoImp(
    private val remoteDataSource: RemoteDataSource
) :CategoryRepo{
    override suspend fun getRecipesOfCategory(categoryName: String) =
        remoteDataSource.getRecipesOfCategory(categoryName)



}
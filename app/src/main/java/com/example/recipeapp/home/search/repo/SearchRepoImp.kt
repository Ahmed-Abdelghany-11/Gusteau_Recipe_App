package com.example.recipeapp.home.search.repo

import com.example.recipeapp.data.remote.RemoteDataSource
import com.example.recipeapp.data.remote.dto.MealList

class SearchRepoImp(
    private val remoteDataSource: RemoteDataSource
): SearchRepository {

    override suspend fun getMealByName(name: String): MealList =
        remoteDataSource.getMealByName(name)

}
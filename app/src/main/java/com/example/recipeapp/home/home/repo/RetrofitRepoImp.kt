package com.example.recipeapp.home.home.repo

import com.example.recipeapp.data.remote.RemoteDataSource

class RetrofitRepoImp (private val remoteDataSource: RemoteDataSource) : RetrofitRepo {
    override suspend fun getMyResponse() = remoteDataSource.getRandomMeal()

    override suspend fun getMealByFirstLetter(letter: String) = remoteDataSource.getMealByFirstLetter(letter)

    override suspend fun getAllCategories() = remoteDataSource.getAllCategories()
}

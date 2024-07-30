package com.example.recipeapp.home.home.repo

import com.example.recipeapp.data.local.LocalDataSource
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.RemoteDataSource

class RetrofitRepoImp (private val remoteDataSource: RemoteDataSource,private val localDataSource: LocalDataSource) : RetrofitRepo {
    override suspend fun getMyResponse() = remoteDataSource.getRandomMeal()

    override suspend fun getMealByFirstLetter(letter: String) = remoteDataSource.getMealByFirstLetter(letter)

    override suspend fun getAllCategories() = remoteDataSource.getAllCategories()

    override suspend fun insertIntoFav(userMealCrossRef: UserMealCrossRef) = localDataSource.insertIntoFav(userMealCrossRef)
}

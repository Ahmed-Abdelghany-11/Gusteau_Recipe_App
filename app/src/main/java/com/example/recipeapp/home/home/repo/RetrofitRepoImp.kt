package com.example.recipeapp.home.home.repo

import com.example.recipeapp.data.remote.RemoteDataSource

class RetrofitRepoImp (private val remoteDataSource: RemoteDataSource) : RetrofitRepo {
    override suspend fun getMyResponse() = remoteDataSource.getRandomMeal()
}

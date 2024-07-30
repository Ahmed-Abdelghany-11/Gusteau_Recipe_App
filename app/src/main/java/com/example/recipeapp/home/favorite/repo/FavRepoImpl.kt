package com.example.recipeapp.home.favorite.repo

import com.example.recipeapp.data.local.LocalDataSource
import com.example.recipeapp.data.local.model.UserWithMeal

class FavRepoImpl(private val localDataSource: LocalDataSource) : FavRepo {

    override suspend fun getAllUserFavMeals(): List<UserWithMeal> {
        return localDataSource.getAllUserFavMeals()
    }

}
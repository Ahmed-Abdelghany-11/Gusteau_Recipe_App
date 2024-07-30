package com.example.recipeapp.home.favorite.repo

import com.example.recipeapp.data.local.LocalDataSource
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.local.model.UserWithMeal
import com.example.recipeapp.data.remote.dto.Meal

class FavRepoImpl(private val localDataSource: LocalDataSource) : FavRepo {

    override suspend fun getAllUserFavMeals(userId : Int): List<Meal>{
        return localDataSource.getAllUserFavMeals(userId)
    }

    override suspend fun deleteFromFav(userWithMeals: UserMealCrossRef) {
        return localDataSource.deleteFromFav(userWithMeals)
    }

}
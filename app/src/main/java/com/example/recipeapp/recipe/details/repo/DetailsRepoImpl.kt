package com.example.recipeapp.recipe.details.repo

import com.example.recipeapp.data.local.LocalDataSource
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.RemoteDataSource
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList

class DetailsRepoImpl(private val localDataSource: LocalDataSource, private val remoteDataSource: RemoteDataSource):
    DetailsRepo {
    override suspend fun insertIntoFav(userMealCrossRef: UserMealCrossRef) {
        localDataSource.insertIntoFav(userMealCrossRef)
    }

    override suspend fun deleteFromFav(userMealCrossRef: UserMealCrossRef) {
        localDataSource.deleteFromFav(userMealCrossRef)
    }

    override suspend fun insertMeal(meal: Meal) {
        localDataSource.insertMeal(meal)
    }

    override suspend fun deleteMeal(meal: Meal) {
        localDataSource.deleteMeal(meal)
    }

    override suspend fun isFavoriteMeal(userId: Int, mealId: String) : Boolean {
        return localDataSource.isFavoriteMeal(userId, mealId)
    }

    override suspend fun getMealById(id: String): MealList {
        return remoteDataSource.getMealById(id)
    }


}
package com.example.recipeapp.details.repo

import com.example.recipeapp.data.local.LocalDataSource
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal

class DetailsRepoImpl(private val localDataSource: LocalDataSource): DetailsRepo {
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


}
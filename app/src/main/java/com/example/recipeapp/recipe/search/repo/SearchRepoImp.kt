package com.example.recipeapp.recipe.search.repo

import com.example.recipeapp.data.local.LocalDataSource
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.RemoteDataSource
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList

class SearchRepoImp(
    private val remoteDataSource: RemoteDataSource,private val localDataSource: LocalDataSource
): SearchRepository {

    override suspend fun getMealByName(name: String): MealList =
        remoteDataSource.getMealByName(name)


    override suspend fun insertIntoFav(userMealCrossRef: UserMealCrossRef) =
        localDataSource.insertIntoFav(userMealCrossRef)

    override suspend fun insertMeal(meal: Meal) =
        localDataSource.insertMeal(meal)

    override suspend fun deleteMeal(meal: Meal) =
        localDataSource.deleteMeal(meal)

    override suspend fun isFavoriteMeal(userId:Int,mealId:String) =
        localDataSource.isFavoriteMeal(userId, mealId)

    override suspend fun deleteFromFav(userMealCrossRef: UserMealCrossRef) =
        localDataSource.deleteFromFav(userMealCrossRef)




}
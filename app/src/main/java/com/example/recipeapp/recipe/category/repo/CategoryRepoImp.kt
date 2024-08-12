package com.example.recipeapp.recipe.category.repo

import com.example.recipeapp.data.local.LocalDataSource
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.RemoteDataSource
import com.example.recipeapp.data.remote.dto.Meal

class CategoryRepoImp(
    private val remoteDataSource: RemoteDataSource,private val localDataSource: LocalDataSource
) : CategoryRepo {
    override suspend fun getRecipesOfCategory(categoryName: String) =
        remoteDataSource.getRecipesOfCategory(categoryName)

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
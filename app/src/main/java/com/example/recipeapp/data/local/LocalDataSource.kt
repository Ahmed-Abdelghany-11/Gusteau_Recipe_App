package com.example.recipeapp.data.local

import com.example.recipeapp.data.local.model.UserData
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.local.model.UserWithMeal
import com.example.recipeapp.data.remote.dto.Meal

interface LocalDataSource {

    suspend fun getUserDataById(id: Int): UserData

    suspend fun isUserExists(email: String, password: String): Boolean

    suspend fun isEmailAlreadyExists(email: String): Boolean

    suspend fun insertUserData(userData: UserData)

    suspend fun getUserIdByEmailAndPassword(email: String,password: String):Int

    suspend fun insertIntoFav(userMealCrossRef: UserMealCrossRef)

    suspend fun deleteFromFav(userMealCrossRef: UserMealCrossRef)

    suspend fun getAllUserFavMeals(userId : Int): List<Meal>

    suspend fun isFavoriteMeal(userId: Int, mealId: String): Boolean

    suspend fun getUserWithMeals(userId: Int): UserWithMeal?

    suspend fun insertMeal(meal: Meal)
    suspend fun deleteMeal(meal: Meal)
}
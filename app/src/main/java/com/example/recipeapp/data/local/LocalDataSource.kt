package com.example.recipeapp.data.local

import com.example.recipeapp.data.local.model.UserData
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.local.model.UserWithMeal

interface LocalDataSource {

    suspend fun getUserDataById(id: Int): UserData

    suspend fun isUserExists(email: String, password: String): Boolean

    suspend fun isEmailAlreadyExists(email: String): Boolean

    suspend fun insertUserData(userData: UserData)

    suspend fun insertIntoFav(userMealCrossRef: UserMealCrossRef)

    suspend fun deleteFromFav(userMealCrossRef: UserMealCrossRef)

    suspend fun getAllUserFavMeals(userId : Int): List<UserWithMeal>

    suspend fun isFavoriteMeal(userId: Int, mealId: String): Boolean

}
package com.example.recipeapp.data.local

import android.content.Context
import com.example.recipeapp.data.local.dao.UserDao
import com.example.recipeapp.data.local.dao.UserWithMealDao
import com.example.recipeapp.data.local.model.UserData
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.local.model.UserWithMeal

class LocalDataSourceImpl(context: Context) : LocalDataSource {

    private var dao: UserDao
    private var userWithMealDao: UserWithMealDao


    init {
        val db = UserDatabase.getInstance(context)
        dao = db.userDao()
        userWithMealDao = db.userWithMealDao()
    }

    override suspend fun getUserDataById(id: Int) =
        dao.getUserDataById(id)

    override suspend fun isUserExists(email: String, password: String) =
        dao.isUserExists(email, password)

    override suspend fun isEmailAlreadyExists(email: String) =
        dao.isEmailAlreadyExists(email)


    override suspend fun insertUserData(userData: UserData) =
        dao.insertUserData(userData)

    override suspend fun insertIntoFav(userWithMeals: UserMealCrossRef) {
        userWithMealDao.insertIntoFav(userWithMeals)
    }

    override suspend fun deleteFromFav(userWithMeals: UserMealCrossRef) {
        userWithMealDao.deleteFromFav(userWithMeals)
    }

    override suspend fun getAllUserFavMeals(): List<UserWithMeal> {
        return userWithMealDao.getAllUserFavMeals()
    }

    override suspend fun isFavoriteMeal(userId: Int, mealId: String): Boolean {
        return userWithMealDao.isFavoriteMeal(userId, mealId)
    }




}
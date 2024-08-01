package com.example.recipeapp.data.local

import android.content.Context
import com.example.recipeapp.data.local.dao.MealDao
import com.example.recipeapp.data.local.dao.UserDao
import com.example.recipeapp.data.local.dao.UserWithMealDao
import com.example.recipeapp.data.local.model.UserData
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.local.model.UserWithMeal
import com.example.recipeapp.data.remote.dto.Meal

class LocalDataSourceImpl(context: Context) : LocalDataSource {

    private var dao: UserDao
    private var userWithMealDao: UserWithMealDao
    private var mealDao: MealDao



    init {
        val db = UserDatabase.getInstance(context)
        dao = db.userDao()
        userWithMealDao = db.userWithMealDao()
        mealDao= db.mealDao()
    }

    override suspend fun getUserDataById(id: Int) =
        dao.getUserDataById(id)

    override suspend fun isUserExists(email: String, password: String) =
        dao.isUserExists(email, password)

    override suspend fun isEmailAlreadyExists(email: String) =
        dao.isEmailAlreadyExists(email)


    override suspend fun insertUserData(userData: UserData) =
        dao.insertUserData(userData)

    override suspend fun getUserIdByEmailAndPassword(email: String, password: String)=
        dao.getUserIdByEmailAndPassword(email,password)


    override suspend fun insertIntoFav(userMealCrossRef: UserMealCrossRef) {
        userWithMealDao.insertIntoFav(userMealCrossRef)
    }

    override suspend fun deleteFromFav(userMealCrossRef: UserMealCrossRef) {
        userWithMealDao.deleteFromFav(userMealCrossRef)
    }

    override suspend fun getAllUserFavMeals(userId : Int): List<Meal> {
        return userWithMealDao.getAllUserFavMeals(userId)
    }

    override suspend fun isFavoriteMeal(userId: Int, mealId: String): Boolean {
        return userWithMealDao.isFavoriteMeal(userId, mealId)
    }

    override suspend fun getUserWithMeals(userId: Int): UserWithMeal? {
        return userWithMealDao.getUserWithMeals(userId)
    }

    override suspend fun insertMeal(meal: Meal) =
        mealDao.insertMeal(meal)



    override suspend fun deleteMeal(meal: Meal) =
        mealDao.deleteMeal(meal)

}
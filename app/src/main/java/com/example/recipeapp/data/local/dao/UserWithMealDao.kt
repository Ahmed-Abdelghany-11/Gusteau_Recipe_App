package com.example.recipeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.local.model.UserWithMeal

@Dao
interface UserWithMealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoFav(userMealCrossRef: UserMealCrossRef)

    @Delete
    suspend fun deleteFromFav(userMealCrossRef: UserMealCrossRef)

    @Transaction
    @Query("SELECT * FROM user WHERE id = :userId")
    suspend fun getAllUserFavMeals(userId : Int): List<UserWithMeal>

    @Transaction
    @Query("SELECT EXISTS (SELECT * FROM UserMealCrossRef  WHERE id = :userId AND idMeal = :mealId)")
    suspend fun isFavoriteMeal(userId: Int, mealId: String): Boolean

}
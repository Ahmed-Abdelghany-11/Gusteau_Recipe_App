package com.example.recipeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.recipeapp.data.local.model.UserData
import com.example.recipeapp.data.local.model.UserWithMeal

@Dao
interface UserDao {

    @Query("SELECT * FROM  user WHERE ID= :id")
     fun getUserDataById(id: Int): UserData

    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE email = :email AND password = :password)")
    suspend fun isUserExists(email: String, password: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM user WHERE email = :email)")
    suspend fun isEmailAlreadyExists(email: String): Boolean

    @Insert
    suspend fun insertUserData(userData: UserData)

}
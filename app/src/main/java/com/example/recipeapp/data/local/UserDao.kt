package com.example.recipeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.recipeapp.data.local.dao.UserData

@Dao
interface UserDao {

    @Query("SELECT * FROM  user WHERE ID= :id")
    suspend fun getUserDataById(id: Int): UserData

    @Insert
    suspend fun insertUserData(userData: UserData)
}
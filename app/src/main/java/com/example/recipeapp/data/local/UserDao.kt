package com.example.recipeapp.data.local

import com.example.recipeapp.data.local.dao.UserData

@Dao
interface UserDao {

    @Query("SELECT * FROM  USERDATA WHERE ID= :id")
    suspend fun getUserDataById(id: Int): UserData

    @Insert
    suspend fun insertUserData(userData: UserData)
}
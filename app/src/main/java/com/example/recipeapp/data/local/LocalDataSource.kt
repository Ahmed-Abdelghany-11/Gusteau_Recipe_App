package com.example.recipeapp.data.local

import com.example.recipeapp.data.local.model.UserData

interface LocalDataSource {

    suspend fun getUserDataById(id: Int): UserData

    suspend fun isUserExists(email: String, password: String): Boolean

    suspend fun isEmailAlreadyExists(email: String): Boolean

    suspend fun insertUserData(userData: UserData)

    suspend fun getUserIdByEmailAndPassword(email: String,password: String):Int

}
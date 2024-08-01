package com.example.recipeapp.authentication.signUp.repo

import com.example.recipeapp.data.local.model.UserData

interface SignUpRepository {

    suspend fun getUserDataById(id: Int): UserData

    suspend fun isEmailAlreadyExists(email: String): Boolean

    suspend fun insertUserData(userData: UserData)

    suspend fun getUserIdByEmailAndPassword(email: String,password: String):Int

}
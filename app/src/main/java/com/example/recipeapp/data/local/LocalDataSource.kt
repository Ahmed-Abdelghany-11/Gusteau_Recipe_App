package com.example.recipeapp.data.local

import com.example.recipeapp.data.local.model.UserData

interface LocalDataSource {

    suspend fun getUserDataById(id: Int): UserData

    suspend fun insertUserData(userData: UserData)

}
package com.example.recipeapp.authentication.signUp.repo

import com.example.recipeapp.data.local.LocalDataSource
import com.example.recipeapp.data.local.model.UserData

class SignUpRepoImp(
    private val localDataSource: LocalDataSource,
) : SignUpRepository {

    override suspend fun getUserDataById(id: Int): UserData =
        localDataSource.getUserDataById(id)

    override suspend fun isEmailAlreadyExists(email: String) =
        localDataSource.isEmailAlreadyExists(email)

    override suspend fun insertUserData(userData: UserData) =
        localDataSource.insertUserData(userData)

}
package com.example.recipeapp.authentication.login.repo

import com.example.recipeapp.data.local.LocalDataSource

class LoginRepoImp(
    private val localDataSource: LocalDataSource,
) : LoginRepository {

    override suspend fun isUserExists(email: String, password: String) =
        localDataSource.isUserExists(email, password)

    override suspend fun isEmailAlreadyExists(email: String) =
        localDataSource.isEmailAlreadyExists(email)

}



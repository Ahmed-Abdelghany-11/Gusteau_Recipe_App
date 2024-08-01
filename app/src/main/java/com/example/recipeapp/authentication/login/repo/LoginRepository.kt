package com.example.recipeapp.authentication.login.repo

interface LoginRepository {
    suspend fun isUserExists(email: String, password: String): Boolean
    suspend fun isEmailAlreadyExists(email: String): Boolean
    suspend fun getUserIdByEmailAndPassword(email: String,password: String):Int

}
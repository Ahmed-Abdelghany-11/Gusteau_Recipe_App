package com.example.recipeapp.data.local

import android.content.Context
import com.example.recipeapp.data.local.dao.UserDao
import com.example.recipeapp.data.local.model.UserData

class LocalDataSourceImpl(context: Context) : LocalDataSource {

    private var dao: UserDao

    init {
        val db = UserDatabase.getInstance(context)
        dao = db.userDao()
    }

    override suspend fun getUserDataById(id: Int) =
        dao.getUserDataById(id)

    override suspend fun isUserExists(email: String, password: String) =
        dao.isUserExists(email, password)

    override suspend fun isEmailAlreadyExists(email: String) =
        dao.isEmailAlreadyExists(email)


    override suspend fun insertUserData(userData: UserData) =
        dao.insertUserData(userData)

}
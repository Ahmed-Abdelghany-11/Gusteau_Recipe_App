package com.example.recipeapp.data.local

import android.content.Context
import com.example.recipeapp.data.local.dao.UserData

class LocalDataSourceImpl(context: Context) : LocalDataSource {

    private lateinit var dao: UserDao

    init {
        val db = UserDatabase.getInstance(context)
        dao = db.userDao()
    }

    override suspend fun getUserDataById(id: Int): UserData {
        return dao.getUserDataById(id)
    }

    override suspend fun insertUserData(userData: UserData) {
        return dao.insertUserData(userData)
    }
}
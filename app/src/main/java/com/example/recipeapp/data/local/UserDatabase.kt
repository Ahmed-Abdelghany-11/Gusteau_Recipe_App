package com.example.recipeapp.data.local

import android.content.Context
import com.example.recipeapp.data.local.dao.UserData

@Database ( entities = [UserData::class], version = 1)
abstract class UserDatabase : RoomDatabase {

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context,
                    UserDatabase::class.java,
                    "user_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { createInstance ->
                        INSTANCE = createInstance
                    }
            }
        }
    }
}
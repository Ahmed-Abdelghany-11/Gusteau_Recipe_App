package com.example.recipeapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.recipeapp.data.local.dao.MealDao
import com.example.recipeapp.data.local.dao.UserDao
import com.example.recipeapp.data.local.dao.UserWithMealDao
import com.example.recipeapp.data.local.model.UserData
import com.example.recipeapp.data.local.model.UserMealCrossRef
import com.example.recipeapp.data.remote.dto.Meal

@Database ( entities = [UserData::class,Meal::class,UserMealCrossRef::class], version = 5)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun userWithMealDao(): UserWithMealDao
    abstract fun mealDao(): MealDao

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
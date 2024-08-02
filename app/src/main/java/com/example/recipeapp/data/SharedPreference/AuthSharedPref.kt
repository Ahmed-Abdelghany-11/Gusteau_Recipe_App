package com.example.recipeapp.data.SharedPreference

import android.content.Context
import android.content.SharedPreferences

class AuthSharedPref(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "user_prefs"
        private const val IS_LOGGED_IN = "IS_LOGGED_IN"
        private const val USER_ID = "user_id"
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(IS_LOGGED_IN, isLoggedIn)
            apply()
        }
    }



    fun isLoggedIn()=
         sharedPreferences.getBoolean(IS_LOGGED_IN, false)

    fun saveUserId(userId:Int){
        with(sharedPreferences.edit()) {
            putInt(USER_ID, userId)
            apply()
        }
    }

    fun getUserId()= sharedPreferences.getInt(USER_ID,0)


    fun clearLoginStatus() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}
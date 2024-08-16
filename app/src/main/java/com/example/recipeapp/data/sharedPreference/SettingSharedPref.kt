package com.example.recipeapp.data.sharedPreference

import android.content.Context
import android.content.SharedPreferences

class SettingSharedPref(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "app_settings"
        private const val DARK_MODE = "dark_mode"
    }

    fun setTheme(isDark: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(DARK_MODE, isDark)
            apply()
        }
    }



    fun getTheme()=
        sharedPreferences.getBoolean(DARK_MODE, false)
}
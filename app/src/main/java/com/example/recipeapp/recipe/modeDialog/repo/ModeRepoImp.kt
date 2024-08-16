package com.example.recipeapp.recipe.modeDialog.repo

import com.example.recipeapp.data.sharedPreference.SettingSharedPref

class ModeRepoImp(
    private val settingSharedPref: SettingSharedPref
) :ModeRepo {
    override fun setTheme(isDark: Boolean)= settingSharedPref.setTheme(isDark)
    override fun getTheme()= settingSharedPref.getTheme()

}
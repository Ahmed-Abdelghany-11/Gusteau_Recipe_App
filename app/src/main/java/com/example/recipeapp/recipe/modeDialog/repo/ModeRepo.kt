package com.example.recipeapp.recipe.modeDialog.repo

interface ModeRepo {
    fun setTheme(isDark:Boolean)
    fun getTheme():Boolean
}
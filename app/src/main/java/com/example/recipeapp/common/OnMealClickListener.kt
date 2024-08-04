package com.example.recipeapp.common

import com.example.recipeapp.data.remote.dto.Meal

interface OnMealClickListener {
    fun onMealClick(meal: Meal)
}
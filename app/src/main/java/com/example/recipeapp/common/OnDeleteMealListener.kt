package com.example.recipeapp.common

import android.widget.ImageView
import com.example.recipeapp.data.remote.dto.Meal

interface OnDeleteMealListener {
    fun confirmDelete(meal: Meal)
}
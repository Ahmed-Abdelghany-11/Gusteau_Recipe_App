package com.example.recipeapp.favorite.view.adapter

import com.example.recipeapp.data.remote.dto.Meal

interface OnFavMealClickListener {
    fun onMealClick(meal: Meal)
}
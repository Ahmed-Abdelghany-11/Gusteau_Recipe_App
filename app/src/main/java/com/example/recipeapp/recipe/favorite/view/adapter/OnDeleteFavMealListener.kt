package com.example.recipeapp.recipe.favorite.view.adapter

import com.example.recipeapp.data.remote.dto.Meal

interface OnDeleteFavMealListener {
    fun confirmDelete(meal: Meal)
    fun cancel(meal: Meal)
}
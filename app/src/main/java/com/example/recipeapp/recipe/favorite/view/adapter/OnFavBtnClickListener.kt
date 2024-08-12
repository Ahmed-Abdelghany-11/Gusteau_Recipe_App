package com.example.recipeapp.recipe.favorite.view.adapter

import com.example.recipeapp.data.remote.dto.Meal

interface OnFavBtnClickListener {

    fun onFavBtnClick(meal: Meal)
}
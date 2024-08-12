package com.example.recipeapp.recipe.common

import android.widget.ImageView
import com.example.recipeapp.data.remote.dto.Meal

interface OnFavBtnClickListener {
    fun onFavBtnClick(meal: Meal, btn: ImageView)

}
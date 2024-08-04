package com.example.recipeapp.category.view.adapter

import android.view.View
import android.widget.ImageView
import com.example.recipeapp.data.remote.dto.Meal

interface OnFavBtnClickListener {
    fun onFavBtnClick(meal: Meal, btn: ImageView)

}
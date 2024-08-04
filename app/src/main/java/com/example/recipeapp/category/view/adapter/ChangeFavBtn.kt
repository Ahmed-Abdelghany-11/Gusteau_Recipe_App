package com.example.recipeapp.category.view.adapter

import android.widget.ImageView
import com.example.recipeapp.data.remote.dto.Meal

interface ChangeFavBtn {
    fun changeFavBtn(meal: Meal,btn:ImageView)
}
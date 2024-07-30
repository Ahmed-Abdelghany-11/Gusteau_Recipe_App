package com.example.recipeapp.data.local.model

import androidx.room.Entity

@Entity(primaryKeys = ["id","idMeal"] )
data class UserMealCrossRef(
    val id: Int,
    val idMeal: String
)

package com.example.recipeapp.data.local.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.recipeapp.data.remote.dto.Meal

data class UserWithMeal(
    @Embedded val user: UserData,
    @Relation(
        parentColumn = "id",
        entityColumn = "idMeal",
        associateBy = Junction(UserMealCrossRef::class)
    )
    val meals: List<Meal>
)

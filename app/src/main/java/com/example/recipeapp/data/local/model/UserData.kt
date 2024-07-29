package com.example.recipeapp.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserData(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,

)

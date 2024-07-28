package com.example.recipeapp.data.local.dao

@Entity (tableName = "user")
data class UserData(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,

)

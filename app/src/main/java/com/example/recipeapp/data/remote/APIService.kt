package com.example.recipeapp.data.remote

import com.example.recipeapp.data.remote.dto.Category
import com.example.recipeapp.data.remote.dto.CategoryList
import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("random.php")
    suspend fun getRandomMeal(): MealList

    @GET("search.php")
    suspend fun getMealByName(
        @Query("s") name: String
    ) : MealList

    @GET("search.php")
    suspend fun getMealByFirstLetter(
        @Query("f") letter: String
    ) : MealList

    @GET("categories.php")
    suspend fun getAllCategories () : CategoryList

    @GET("lookup.php?")
    suspend fun getMealById(@Query("i") id : String): MealList

}
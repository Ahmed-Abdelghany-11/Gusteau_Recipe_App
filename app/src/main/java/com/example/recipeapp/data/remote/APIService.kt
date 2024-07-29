package com.example.recipeapp.data.remote

import com.example.recipeapp.data.remote.dto.Meal
import com.example.recipeapp.data.remote.dto.MealList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.Locale.Category

interface APIService {

    @GET("random.php")
    suspend fun getRandomMeal(): MealList

    @GET("search.php?s={name}")
    suspend fun getMealByName(
        @Path("name") name: String
    ) : MealList

    @GET("search.php?f={letter}")
    suspend fun getMealByFirstLetter(
        @Path("letter") letter: String
    ) : MealList

    @GET("categories.php")
    suspend fun getAllCategories () : List<Category>

}
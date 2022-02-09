package com.marta.islandcook.provider.api

import com.marta.islandcook.model.RecipeResponse
import retrofit2.http.GET

interface NetworkService {
    @GET("api/recipe/")
    suspend fun getAllRecipes(): List<RecipeResponse>
}
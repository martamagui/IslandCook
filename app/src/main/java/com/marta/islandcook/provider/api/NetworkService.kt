package com.marta.islandcook.provider.api

import com.marta.islandcook.model.body.RecipeBody
import com.marta.islandcook.model.response.RecipeResponse
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {
    //-------------------------------------- GET REQUEST
    @GET("api/recipe/")
    suspend fun getRecipesList(): List<RecipeResponse>

    @GET("api/recipe/{id}")
    suspend fun getRecipeById(@Path("id") id: String): RecipeResponse

    @GET("api/recipe/tag/{tag}")
    suspend fun getRecipeListByTag(@Path("tag") tag: String): List<RecipeResponse>

    @GET("/api/recipe/difficulty/{difficulty}")
    suspend fun getRecipeListByDifficulty(@Path("difficulty") difficulty: String): List<RecipeResponse>

    //-------------------------------------- POST REQUEST
    @POST("api/recipe/")
    suspend fun addRecipe(@Body body: RecipeBody): RecipeResponse

    //-------------------------------------- PUT REQUEST
    @PUT("api/recipe/{id}")
    suspend fun editRecipe(@Path("id") id: String): RecipeResponse

    //-------------------------------------- DELTE REQUEST
    @DELETE("api/recipe/{id}")
    suspend fun deleteRecipe(@Path("id") id: String): Any

}
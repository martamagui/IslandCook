package com.marta.islandcook.provider.api

import com.marta.islandcook.model.body.RecipeBody
import com.marta.islandcook.model.response.RecipeResponse
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {
    //-------------------------------------- GET REQUEST
    @GET("api/recipe/")
    suspend fun getRecipesList(): Call<List<RecipeResponse>>

    @GET("api/recipe/{id}")
    suspend fun getRecipeById(@Path("id") id: String): Call<RecipeResponse>

    @GET("api/recipe/tag/{tag}")
    suspend fun getRecipeListByTag(@Path("tag") tag: String): Call<List<RecipeResponse>>

    @GET("/api/recipe/difficulty/{difficulty}")
    suspend fun getRecipeListByDifficulty(@Path("difficulty") difficulty: String): Call<List<RecipeResponse>>

    //-------------------------------------- POST REQUEST
    @POST("api/recipe/")
    suspend fun addRecipe(@Body body: RecipeBody): Call<RecipeResponse>

    //-------------------------------------- PUT REQUEST
    @PUT("api/recipe/{id}")
    suspend fun editRecipe(@Path("id") id: String): Call<RecipeResponse>

    //-------------------------------------- DELTE REQUEST
    @DELETE("api/recipe/{id}")
    suspend fun deleteRecipe(@Path("id") id: String): Call<Any>

}
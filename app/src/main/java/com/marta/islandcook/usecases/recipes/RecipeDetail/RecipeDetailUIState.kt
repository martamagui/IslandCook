package com.marta.islandcook.usecases.recipes.RecipeDetail

import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.db.entities.Recipies

data class RecipeDetailUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val recipeList: List<RecipeResponse>? = null,
    val likedRecipies: List<String>? = null
)



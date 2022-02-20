package com.marta.islandcook.usecases.recipes.RecipeDetail

import com.marta.islandcook.model.response.RecipeResponse

data class RecipeDetailUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val isDeleted: Boolean = false,
    val recipe: RecipeResponse? = null,
    val likedRecipies: List<String>? = null
)



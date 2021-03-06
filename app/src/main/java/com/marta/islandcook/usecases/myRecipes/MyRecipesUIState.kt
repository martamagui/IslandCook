package com.marta.islandcook.usecases.myRecipes

import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.db.entities.Recipies

data class MyRecipesUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val recipeListDB: List<Recipies>? = null,
)
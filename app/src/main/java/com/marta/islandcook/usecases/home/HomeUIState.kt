package com.marta.islandcook.usecases.home

import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.db.entities.Recipies

data class HomeUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val recipeList: List<RecipeResponse>? = null,
    val recipeListDB: List<Recipies>? = null,
    val likedRecipies: List<String>? = null
)

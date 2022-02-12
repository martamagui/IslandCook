package com.marta.islandcook.usecases.home

import com.marta.islandcook.model.response.RecipeResponse

data class HomeUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val recipeList: List<RecipeResponse>? = null,
    val recipeListDB: List<RecipeResponse>? = null
)

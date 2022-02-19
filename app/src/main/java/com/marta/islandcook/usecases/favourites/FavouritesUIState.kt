package com.marta.islandcook.usecases.favourites

import com.marta.islandcook.provider.db.entities.Recipies

data class FavouritesUIState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val recipeListDB: List<Recipies>? = null
)

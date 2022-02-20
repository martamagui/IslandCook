package com.marta.islandcook.usecases.personal.add

import com.marta.islandcook.model.response.RecipeResponse

data class AddEditUIState(
    val isEdit: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val addedToAPI: Boolean = false,
    val recipe: RecipeResponse? = null,
    val recipeIdForDB: String? = null
)



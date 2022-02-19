package com.marta.islandcook.provider.db

import androidx.room.Embedded
import androidx.room.Relation
import com.marta.islandcook.provider.db.entities.Ingredients
import com.marta.islandcook.provider.db.entities.Recipies
import com.marta.islandcook.provider.db.entities.Steps

class RecipeSteps (
    @Embedded val recipies: Recipies,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "stepsRecipeId"
    )
    val steps: Steps
)
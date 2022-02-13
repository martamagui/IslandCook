package com.marta.islandcook.provider.db

import androidx.room.Embedded
import androidx.room.Relation

class RecipieIngredients (
    @Embedded val recipies: Recipies,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "ingredientId"
    )
    val ingredients: Ingredients
)
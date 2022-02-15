package com.marta.islandcook.provider.db

import androidx.room.Embedded
import androidx.room.Relation
import com.marta.islandcook.provider.db.entities.Ingredients
import com.marta.islandcook.provider.db.entities.Recipies

class RecipieIngredients (
    @Embedded val recipies: Recipies,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "ingredientId"
    )
    val ingredients: Ingredients
)
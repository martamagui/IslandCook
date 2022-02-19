package com.marta.islandcook.provider.db

import androidx.room.Embedded
import androidx.room.Relation
import com.marta.islandcook.provider.db.entities.Ingredients
import com.marta.islandcook.provider.db.entities.Recipies
import com.marta.islandcook.provider.db.entities.Tags

class RecipeTags (
    @Embedded val recipies: Recipies,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "tagsRecipeId"
    )
    val tags: Tags
)
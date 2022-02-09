package com.marta.islandcook.model.body

import com.google.gson.annotations.SerializedName
import com.marta.islandcook.model.response.Ingredient

class RecipeBody(
    val id: String,
    val name: String,
    val ingredients: List<Ingredient>,
    val steps: List<String>,
    val pictureUrl: String,
    val difficulty: String,
    val author: String,
    val tags: List<String>
)


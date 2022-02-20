package com.marta.islandcook.model.body

import com.google.gson.annotations.SerializedName
import com.marta.islandcook.model.response.Ingredient

class RecipeBody(
    var name: String,
    var steps: List<String>,
    var ingredients: List<Ingredient>,
    var picture_url: String,
    var difficulty: String,
    var author: String,
    var tags: List<String>
)


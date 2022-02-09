package com.marta.islandcook.model


import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("ingredients")
    val ingredients: List<Ingredient>,
    @SerializedName("steps")
    val steps: List<String>,
    @SerializedName("picture_url")
    val pictureUrl: String,
    @SerializedName("difficulty")
    val difficulty: String,
    @SerializedName("author")
    val author: String,
    @SerializedName("tags")
    val tags: List<String>
)
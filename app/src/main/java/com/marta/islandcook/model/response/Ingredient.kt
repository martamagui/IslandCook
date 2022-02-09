package com.marta.islandcook.model.response


import com.google.gson.annotations.SerializedName

data class Ingredient(
    @SerializedName("_id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("amount")
    val amount: String
)
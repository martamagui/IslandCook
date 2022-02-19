package com.marta.islandcook.provider.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recipies")
data class Recipies(
    @PrimaryKey(autoGenerate = false)
    val recipeId: String,
    @ColumnInfo(name = "name")
    val name: String,
//    @ColumnInfo(name = "ingredients")
//    val ingredients: String,
//    @ColumnInfo(name = "steps")
//    val steps: String,
    @ColumnInfo(name = "picture_url")
    val picture_url: String,
    @ColumnInfo(name = "difficulty")
    val difficulty: String,
    @ColumnInfo(name = "author")
    val author: String,
//    @ColumnInfo(name = "tags")
//    val tags: String,
    @ColumnInfo(name = "myRecipies")
    val myRecipies: Boolean
)
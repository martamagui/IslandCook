package com.marta.islandcook.provider.db

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recipies")
data class Recipies(
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "ingredients")
    //Segunda tabla?
    val ingredients: String,
    @ColumnInfo(name = "steps")
    val steps: Array<String>,
    @ColumnInfo(name = "picture_url")
    val picture_url: String,
    @ColumnInfo(name = "difficulty")
    val difficulty: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "tags")
    val tags: Array<String>,
    @PrimaryKey(autoGenerate = false)
    val id: Int
)
package com.marta.islandcook.provider.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Steps")
class Steps (
    @PrimaryKey
    val stepsRecipeId: String,
    @ColumnInfo(name = "steps")
    val steps: String
)
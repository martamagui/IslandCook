package com.marta.islandcook.provider.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Ingredients")
class Ingredients (
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "amount")
    val amount: String,
    @PrimaryKey(autoGenerate = false)
    val ingredientId: Int
)
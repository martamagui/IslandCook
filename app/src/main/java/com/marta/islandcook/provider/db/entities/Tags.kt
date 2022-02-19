package com.marta.islandcook.provider.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Tags")
class Tags (
    @PrimaryKey(autoGenerate = true)
    val tagsRecipeId: String,
    @ColumnInfo(name = "tags")
    val tags: List<String>
)
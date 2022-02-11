package com.marta.islandcook.provider.db

import androidx.room.*


@Dao
interface RecipiesDAO {
    @Query("SELECT * FROM Recipies")
    fun findAllRecipies(): List<Recipies>

    @Query("SELECT * FROM Recipies WHERE recipies.id = :recipiesId")
    fun findRecipiesById(recipiesId: Int): Recipies

    @Query("SELECT * FROM Recipies WHERE recipies.tags = :recipiesTags")
    fun findRecipiesByTags(recipiesTags: Array<String>): Recipies

    @Query("SELECT * FROM Recipies WHERE recipies.difficulty = :recipiesDifficulty")
    fun findRecipiesByDifficulty(recipiesDifficulty: String): Recipies

    //Falta Query de findByIngredient

    @Delete
    fun deleteRecipie(recipies: Recipies)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipies(recipies: Recipies)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecipies(recipies: List<Recipies>)

    @Query("UPDATE Recipies SET name = :recipiesName, steps = :recipiesSteps, picture_url = :recipiesPicture_url, difficulty = :recipiesDifficulty, author = :recipiesAuthor AND tags = :recipiesTags WHERE id = :recipiesId")
    fun updateRecipies(recipiesName: String, recipiesSteps: Array<String>, recipiesPicture_url: String, recipiesDifficulty: String, recipiesAuthor: String, recipiesTags: Array<String>, recipiesId: Int)
}
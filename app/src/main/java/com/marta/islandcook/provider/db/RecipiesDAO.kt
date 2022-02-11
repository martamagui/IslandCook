package com.marta.islandcook.provider.db

import androidx.room.*


@Dao
interface RecipiesDAO {

    //RECIPIES
    @Query("SELECT * FROM Recipies")
    fun findAllRecipies(): List<Recipies>

    @Query("SELECT * FROM Recipies WHERE recipies.recipeId = :recipiesId")
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

    @Query("UPDATE Recipies SET name = :recipiesName, steps = :recipiesSteps, picture_url = :recipiesPicture_url, difficulty = :recipiesDifficulty, author = :recipiesAuthor AND tags = :recipiesTags WHERE recipeId = :recipiesId")
    fun updateRecipies(recipiesName: String, recipiesSteps: Array<String>, recipiesPicture_url: String, recipiesDifficulty: String, recipiesAuthor: String, recipiesTags: Array<String>, recipiesId: Int)




    //INGREDIENTES

    @Query("SELECT * FROM Ingredients")
    fun findAllIngredients(): List<Ingredients>

    @Query("SELECT * FROM Ingredients WHERE ingredients.ingredientId = :ingredientId")
    fun findIngredientsById(ingredientId: Int): Ingredients

    @Delete
    fun deleteIngredient(ingredients: Ingredients)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredients(ingredients: Ingredients)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertIngredients(ingredientes: List<Ingredients>)

    @Query("UPDATE Ingredients SET name = :ingredientsName, amount = :ingredientsAmount WHERE ingredientId = :ingredientsId")
    fun updateIngredients(ingredientsName: String, ingredientsAmount: String, ingredientsId: Int)
}
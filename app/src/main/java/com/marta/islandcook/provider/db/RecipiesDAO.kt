package com.marta.islandcook.provider.db

import androidx.room.*
import com.marta.islandcook.provider.db.entities.Ingredients
import com.marta.islandcook.provider.db.entities.Recipies


@Dao
interface RecipiesDAO {

    //RECIPIES
    @Query("SELECT * FROM Recipies")
    suspend fun findAllRecipies(): List<Recipies>

    @Query("SELECT * FROM Recipies WHERE recipies.recipeId = :recipiesId")
    suspend fun findRecipiesById(recipiesId: String): Recipies


    @Query("SELECT * FROM Recipies WHERE recipies.myRecipies = :recipiesMyRecipies")
    suspend fun dindByMyRecipies(recipiesMyRecipies: Boolean): Recipies

    @Query("SELECT * FROM Recipies WHERE recipies.myRecipies = :recipiesMyRecipies")
    suspend fun findByMyRecipies(recipiesMyRecipies: Boolean):  List<Recipies>

//    @Query("SELECT * FROM Recipies WHERE recipies.tags = :recipiesTags")
//    fun findRecipiesByTags(recipiesTags: Array<String>): Recipies

    @Query("SELECT * FROM Recipies WHERE recipies.difficulty = :recipiesDifficulty")
    suspend fun findRecipiesByDifficulty(recipiesDifficulty: String): Recipies

    @Query("DELETE FROM Recipies WHERE recipies.recipeId = :recipeId")
    suspend fun deleteRecipieById(recipeId: String )

    @Delete
    suspend fun deleteRecipie(recipies: Recipies)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipies(recipies: Recipies)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipies(recipies: List<Recipies>)


    @Query("UPDATE Recipies SET name = :recipiesName, picture_url = :recipiesPicture_url, difficulty = :recipiesDifficulty,myRecipies = :myRecipe, author = :recipiesAuthor WHERE recipeId = :recipiesId")
    suspend fun updateRecipies(recipiesName: String, recipiesPicture_url: String, recipiesDifficulty: String, recipiesAuthor: String, recipiesId: String, myRecipe: Boolean)

    @Query("UPDATE Steps SET steps = :steps")
    suspend fun updateSteps(steps: List<String>)

    @Query("UPDATE Tags SET tags = :tags")
    suspend fun updateTags(tags: List<String>)

    @Update
    suspend fun updateIngredients(ingredients: List<Ingredients>)
    //INGREDIENTES


    @Query("SELECT * FROM Ingredients")
    suspend fun findAllIngredients(): List<Ingredients>

    @Query("SELECT * FROM Ingredients WHERE ingredients.ingredientId = :ingredientId")
    suspend fun findIngredientsById(ingredientId: Int): Ingredients

    @Delete
    suspend fun deleteIngredient(ingredients: Ingredients)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: Ingredients)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredientes: List<Ingredients>)



}
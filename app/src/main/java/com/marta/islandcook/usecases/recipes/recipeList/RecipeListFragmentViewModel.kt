package com.marta.islandcook.usecases.recipes.recipeList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.api.NetworkManagerRecipesAPI
import com.marta.islandcook.provider.api.NetworkService
import com.marta.islandcook.provider.db.IslandCook_Database
import com.marta.islandcook.provider.db.entities.Recipies
import com.marta.islandcook.usecases.home.HomeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeListFragmentViewModel @Inject constructor(private val networkService: NetworkService, private val db: IslandCook_Database)  : ViewModel() {
    private val _recipeListUIState: MutableStateFlow<RecipeListUIState> =
        MutableStateFlow(RecipeListUIState())
    val recipeListUIState: StateFlow<RecipeListUIState>
        get() = _recipeListUIState

    //------------------------ API REQUEST
    fun getRecipesFromAPIbyTag(tag: String) {
        _recipeListUIState.update { RecipeListUIState(isLoading = true, recipeList = null) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                delay(100)
                if (tag != "") {
                    val recipes: List<RecipeResponse> =
                        networkService.getRecipeListByTag(tag)
                    updateUIStateList(recipes)
                } else {
                    val recipes: List<RecipeResponse> =
                        networkService.getRecipesList()
                    updateUIStateList(recipes)
                }
            } catch (e: Exception) {
                notifyErrorUIState(e)
            }
        }
    }

    fun getRecipesFromAPIbyDifficulty(diffiulty: String) {
        _recipeListUIState.update { RecipeListUIState(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipes: List<RecipeResponse> =
                    networkService.getRecipeListByDifficulty(diffiulty)
                updateUIStateList(recipes)
            } catch (e: Exception) {
                notifyErrorUIState(e)
            }
        }
    }

    fun getRecipesFromAPIbyTagAndDifficulty(filter: String, difficultity: String) {
        _recipeListUIState.update { RecipeListUIState(isLoading = true, isSuccess = false) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipes: List<RecipeResponse> =
                    networkService.getRecipeListByTagAndDifficulty(
                        filter,
                        difficultity
                    )
                updateUIStateList(recipes)
            } catch (e: Exception) {
                notifyErrorUIState(e)
            }
        }
    }

    //------------------------ UIStateUpdates
    fun updateUIStateList(list: List<RecipeResponse>) {
        _recipeListUIState.update {
            RecipeListUIState(isLoading = false, isSuccess = true, recipeList = list)
        }
    }

    fun notifyErrorUIState(e: Exception) {
        _recipeListUIState.update {
            RecipeListUIState(isLoading = false, isError = true)
        }
        Log.e("ListFViewModel", "Error: $e")
    }
    //------------------------ DB REQUEST
    fun dbRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            val savedRecipes = db.recipiesDao().findAllRecipies()
            var likedRecipes: MutableList<String> = mutableListOf()
            savedRecipes.forEach {
                if (!it.myRecipies) {
                    likedRecipes.add(it.recipeId)
                }
            }
            HomeUIState(likedRecipies = likedRecipes)
        }
    }

    fun saveRecipe(item: RecipeResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            db.recipiesDao().insertRecipies(
                Recipies(
                    item.id,
                    item.name,
                    item.pictureUrl,
                    item.difficulty,
                    item.author,
                    false
                )
            )
            dbRecipes()
        }
    }

    fun dislike(item: RecipeResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            db.recipiesDao().deleteRecipieById(item.id)
            dbRecipes()
        }
    }
}
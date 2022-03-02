package com.marta.islandcook.usecases.recipes.RecipeDetail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
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
class RecipeDetailViewModel @Inject constructor(
    private val networkService: NetworkService,
    private val db: IslandCook_Database,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var _detailUIState: MutableStateFlow<RecipeDetailUIState> =
        MutableStateFlow(RecipeDetailUIState())
    val detailUIState: StateFlow<RecipeDetailUIState>
        get() = _detailUIState

    init {
        val recipeId = savedStateHandle.get<String>("recipeId")
        Log.d("VM", "recipeId: $recipeId")
        recipeId?.let {
            getRecipeFromAPI(recipeId)
        }
    }

    //------------------------ API REQUEST
    fun getRecipeFromAPI(id: String) {
        _detailUIState.update { RecipeDetailUIState(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            delay(100)
            try {
                val recipe: RecipeResponse =
                    networkService.getRecipeById(id)
                updateUIData(recipe)
            } catch (e: Exception) {
                notifyErrorUIState(e)
            }
        }
    }

    fun deleteFromAPI(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                networkService.deleteRecipe(id)
                _detailUIState.update { RecipeDetailUIState(isDeleted = true) }
            } catch (e: Exception) {
                notifyErrorUIState(e)
            }
        }
    }

    //------------------------ UIStateUpdates
    fun updateUIData(recipe: RecipeResponse) {
        _detailUIState.update {
            RecipeDetailUIState(isLoading = false, isSuccess = true, recipe = recipe)
        }
    }

    fun notifyErrorUIState(e: Exception) {
        _detailUIState.update {
            RecipeDetailUIState(isLoading = false, isError = true)
        }
        Log.e("ListFViewModel", "Error: $e")
    }

    //------------------------ DB REQUEST

    fun deleteInDB(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            db.recipiesDao().deleteRecipieById(id)
            dbRecipes()
        }
    }


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
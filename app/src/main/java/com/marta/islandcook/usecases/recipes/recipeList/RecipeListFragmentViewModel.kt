package com.marta.islandcook.usecases.recipes.recipeList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.api.NetworkManagerRecipesAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RecipeListFragmentViewModel : ViewModel() {
    private val _recipeListUIState: MutableStateFlow<RecipeListUIState> =
        MutableStateFlow(RecipeListUIState())
    val recipeListUIState: StateFlow<RecipeListUIState>
        get() = _recipeListUIState

    //------------------------ API REQUEST
    fun getRecipesFromAPIbyTag(tag: String) {
        _recipeListUIState.update { RecipeListUIState(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            delay(500)
            try {
                if (tag != "") {
                    val recipes: List<RecipeResponse> =
                        NetworkManagerRecipesAPI.service.getRecipeListByTag(tag)
                    updateUIStateList(recipes)
                } else {
                    val recipes: List<RecipeResponse> =
                        NetworkManagerRecipesAPI.service.getRecipesList()
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
                    NetworkManagerRecipesAPI.service.getRecipeListByDifficulty(diffiulty)
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
                    NetworkManagerRecipesAPI.service.getRecipeListByTagAndDifficulty(
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
}
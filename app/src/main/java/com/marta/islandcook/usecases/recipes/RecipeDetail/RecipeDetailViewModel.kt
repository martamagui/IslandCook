package com.marta.islandcook.usecases.recipes.RecipeDetail

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

class RecipeDetailViewModel : ViewModel() {
    private var _detailUIState: MutableStateFlow<RecipeDetailUIState> =
        MutableStateFlow(RecipeDetailUIState())
    val detailUIState: StateFlow<RecipeDetailUIState>
        get() = _detailUIState

    //------------------------ API REQUEST
    fun getRecipeFromAPI(id: String) {
        _detailUIState.update { RecipeDetailUIState(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            delay(100)
            try {
                val recipe: RecipeResponse =
                    NetworkManagerRecipesAPI.service.getRecipeById(id)
                updateUIData(recipe)
            } catch (e: Exception) {
                notifyErrorUIState(e)
            }
        }
    }

    fun deleteFromAPI(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                NetworkManagerRecipesAPI.service.deleteRecipe(id)
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
}
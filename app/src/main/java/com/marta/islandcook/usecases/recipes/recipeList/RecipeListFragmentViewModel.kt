package com.marta.islandcook.usecases.recipes.recipeList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.api.NetworkManagerRecipesAPI
import com.marta.islandcook.usecases.home.HomeUIState
import kotlinx.coroutines.Dispatchers
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
    fun getRecipesFromAPI(tag: String, diffiulty: String?, search: String?) {
        _recipeListUIState.update { RecipeListUIState(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if(diffiulty!=""){
                    var recipes: List<RecipeResponse> =  NetworkManagerRecipesAPI.service.getRecipeListByTag(tag)
                    updateUIStateList(recipes)
                }else{
                    //TODO Hacer una búsqueda por nombre o dificultad conjunta
                    var recipes: List<RecipeResponse> =  NetworkManagerRecipesAPI.service.getRecipeListByTag(tag)
                    updateUIStateList(recipes)
                }
            } catch (e: Exception) {
                notifyErrorUIState(e)
            }
        }
    }
    //------------------------ UIStateUpdates
    fun updateUIStateList(list: List<RecipeResponse>){
        _recipeListUIState.update {
            RecipeListUIState(isLoading = false, isSuccess = true, recipeList = list)
        }
    }
    fun notifyErrorUIState(e: Exception){
        _recipeListUIState.update {
            RecipeListUIState(isLoading = false, isError = true)
        }
        Log.e("ListFViewModel","Error: $e")
    }
}
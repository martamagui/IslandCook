package com.marta.islandcook.usecases.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.api.NetworkManagerRecipesAPI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call

class HomeFragmentViewModel : ViewModel() {
    private val _homeUIState: MutableStateFlow<HomeUIState> = MutableStateFlow(HomeUIState())
    val homeUIState: StateFlow<HomeUIState>
        get() = _homeUIState

    //------------------------ API REQUEST
    fun getRecipes() {
        _homeUIState.update { HomeUIState(isLoading = true) }
        viewModelScope.launch {
            try {
                val recipes: List<RecipeResponse> = NetworkManagerRecipesAPI.service.getRecipesList()
                _homeUIState.update {
                    HomeUIState(isLoading = false, isSuccess = true, recipeList = recipes)
                }
            }catch (e:Exception){
                _homeUIState.update {
                    HomeUIState(isLoading = false, isError = true)
                }
            }
        }
    }
}
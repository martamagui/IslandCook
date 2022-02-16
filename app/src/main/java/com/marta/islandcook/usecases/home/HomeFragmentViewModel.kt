package com.marta.islandcook.usecases.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.api.NetworkManagerRecipesAPI
import com.marta.islandcook.provider.db.IslandCook_Database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragmentViewModel : ViewModel() {
    private val _homeUIState: MutableStateFlow<HomeUIState> = MutableStateFlow(HomeUIState())
    val homeUIState: StateFlow<HomeUIState>
        get() = _homeUIState

    //------------------------ API REQUEST
    fun getRecipesFromAPI() {
        _homeUIState.update { HomeUIState(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipes: List<RecipeResponse> =
                    NetworkManagerRecipesAPI.service.getRecipesList()
                _homeUIState.update {
                    HomeUIState(isLoading = false, isSuccess = true, recipeList = recipes)
                }
            } catch (e: Exception) {
                _homeUIState.update {
                    HomeUIState(isLoading = false, isError = true)
                }
            }
        }
    }
}
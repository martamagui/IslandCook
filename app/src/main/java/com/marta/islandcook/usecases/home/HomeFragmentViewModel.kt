package com.marta.islandcook.usecases.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.api.NetworkManagerRecipesAPI
import com.marta.islandcook.provider.api.NetworkService
import com.marta.islandcook.provider.db.IslandCook_Database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel  @Inject constructor(private val networkService: NetworkService) : ViewModel() {
    private val _homeUIState: MutableStateFlow<HomeUIState> = MutableStateFlow(HomeUIState())
    val homeUIState: StateFlow<HomeUIState>
        get() = _homeUIState

    //------------------------ API REQUEST
    fun getRecipesFromAPI() {
        _homeUIState.update { HomeUIState(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipes: List<RecipeResponse> =
                   networkService.getRecipesList()
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
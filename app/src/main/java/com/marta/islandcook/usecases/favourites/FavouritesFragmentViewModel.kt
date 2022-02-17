package com.marta.islandcook.usecases.favourites

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

class FavouritesFragmentViewModel:ViewModel() {
    private val _favouritesUIState: MutableStateFlow<FavouritesUIState> = MutableStateFlow(FavouritesUIState())
    val favouritesUIState: StateFlow<FavouritesUIState>
        get() = _favouritesUIState

}
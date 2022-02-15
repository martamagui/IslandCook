package com.marta.islandcook.usecases.favourites

import androidx.lifecycle.ViewModel
import com.marta.islandcook.usecases.home.HomeUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavouritesFragmentViewModel:ViewModel() {
    private val _favouritesUIState: MutableStateFlow<FavouritesUIState> = MutableStateFlow(FavouritesUIState())
    val favouritesUIState: StateFlow<FavouritesUIState>
        get() = _favouritesUIState
}
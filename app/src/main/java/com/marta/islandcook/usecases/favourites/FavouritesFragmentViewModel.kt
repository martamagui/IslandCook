package com.marta.islandcook.usecases.favourites

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class FavouritesFragmentViewModel : ViewModel() {
    private val _favouritesUIState: MutableStateFlow<FavouritesUIState> =
        MutableStateFlow(FavouritesUIState())
    val favouritesUIState: StateFlow<FavouritesUIState>
        get() = _favouritesUIState
}
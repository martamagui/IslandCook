package com.marta.islandcook.usecases.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.marta.islandcook.provider.api.NetworkService
import com.marta.islandcook.provider.db.IslandCook_Database
import com.marta.islandcook.provider.db.entities.Recipies
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class FavouritesFragmentViewModel @Inject constructor(private val db: IslandCook_Database) : ViewModel() {
    private val _favouritesUIState: MutableStateFlow<FavouritesUIState> =
        MutableStateFlow(FavouritesUIState())
    val favouritesUIState: StateFlow<FavouritesUIState>
        get() = _favouritesUIState

    //------------------------ DB REQUEST
    fun getLikedRecipes() {
        _favouritesUIState.update { FavouritesUIState(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            val recipesList = db.recipiesDao().findAllRecipies().toMutableList()
            _favouritesUIState.update {
                FavouritesUIState(
                    recipeListDB = recipesList,
                    isLoading = false,
                    isSuccess = true
                )
            }
        }
    }

    fun dislike(item: Recipies) {
        _favouritesUIState.update { FavouritesUIState(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.recipiesDao().deleteRecipie(item)
                getLikedRecipes()
            } catch (e: Exception) {
                _favouritesUIState.update { FavouritesUIState(isLoading = false, isError = false) }
            }
        }
    }
}
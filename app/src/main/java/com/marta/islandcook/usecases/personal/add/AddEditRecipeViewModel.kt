package com.marta.islandcook.usecases.personal.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marta.islandcook.model.body.RecipeBody
import com.marta.islandcook.provider.api.NetworkService
import com.marta.islandcook.provider.db.IslandCook_Database
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditRecipeViewModel @Inject constructor(private val networkService: NetworkService, private val db: IslandCook_Database) :
    ViewModel() {
    val _addEditUIState: MutableStateFlow<AddEditUIState> = MutableStateFlow(AddEditUIState())
    val addEditUIState: StateFlow<AddEditUIState>
        get() = _addEditUIState

    //------------------------ API REQUEST
    fun addRecipe(recipe: RecipeBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                networkService.addRecipe(recipe)
                _addEditUIState.update {
                    AddEditUIState(isSuccess = true)
                }
            } catch (e: Exception) {
                _addEditUIState.update {
                    AddEditUIState(isError = true)
                }
            }
        }
    }
    fun putRecipe(id: String, recipe: RecipeBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                networkService.editRecipe(id,recipe)
                _addEditUIState.update {
                    AddEditUIState(isSuccess = true)
                }
            } catch (e: Exception) {
                _addEditUIState.update {
                    AddEditUIState(isError = true)
                }
            }
        }
    }

    //------------------------ DB REQUEST

}

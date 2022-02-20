package com.marta.islandcook.usecases.myRecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marta.islandcook.provider.api.NetworkService
import com.marta.islandcook.provider.db.IslandCook_Database
import com.marta.islandcook.usecases.personal.add.AddEditUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyRecipesFragmentViewModel @Inject constructor(
    private val db: IslandCook_Database
) : ViewModel() {

    private val _myRecipesUIState: MutableStateFlow<MyRecipesUIState> =
        MutableStateFlow(MyRecipesUIState())
    val myRecipesUIState: StateFlow<MyRecipesUIState>
        get() = _myRecipesUIState


    fun getRecipesFromDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipes = db.recipiesDao().findByMyRecipies(true).toMutableList()
                _myRecipesUIState.update {
                    MyRecipesUIState(recipeListDB = recipes, isLoading = false, isSuccess = true)
                }
            } catch (e: Exception) {

            }
        }
    }
}
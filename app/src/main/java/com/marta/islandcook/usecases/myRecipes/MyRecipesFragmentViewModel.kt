package com.marta.islandcook.usecases.myRecipes

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MyRecipesFragmentViewModel : ViewModel() {
    private val _myRecipesUIState: MutableStateFlow<MyRecipesUIState> =
        MutableStateFlow(MyRecipesUIState())
    val myRecipesUIState: StateFlow<MyRecipesUIState>
        get() = _myRecipesUIState
}
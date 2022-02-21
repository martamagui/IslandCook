package com.marta.islandcook.usecases.personal.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marta.islandcook.model.body.RecipeBody
import com.marta.islandcook.provider.api.NetworkService
import com.marta.islandcook.provider.db.IslandCook_Database
import com.marta.islandcook.provider.db.entities.Recipies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditRecipeViewModel @Inject constructor(
    private val networkService: NetworkService,
    private val db: IslandCook_Database
) :
    ViewModel() {
    val _addEditUIState: MutableStateFlow<AddEditUIState> = MutableStateFlow(AddEditUIState())
    val addEditUIState: StateFlow<AddEditUIState>
        get() = _addEditUIState

    //------------------------ API REQUEST
    fun getRecipe(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipe = networkService.getRecipeById(id)
                _addEditUIState.update {
                    AddEditUIState( isEdit = true, recipe = recipe)
                }
            } catch (e: Exception) {
                _addEditUIState.update {
                    AddEditUIState(isError = true)
                }
            }
        }
    }

    fun addRecipe(recipe: RecipeBody,recipeDb: Recipies) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val recipe = networkService.addRecipe(recipe)
                var recipeDb = Recipies(recipe.id,recipe.name,recipe.pictureUrl,recipe.difficulty,recipe.author,true)
                Log.e("ID", "${recipe.id}")
                insertRecipeDB(recipeDb)
                _addEditUIState.update {
                    AddEditUIState(isSuccess = true, addedToAPI=true, recipeIdForDB = recipe.id)
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
                networkService.editRecipe(id, recipe)
            } catch (e: Exception) {
                _addEditUIState.update {
                    AddEditUIState(isError = true)
                }
            }
        }
    }


    //------------------------ DB REQUEST

    fun updateRecipeDB(name: String, urlImage: String, difficulty: String, author: String, recipeToEditDB: String, myRecipe: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            db.recipiesDao()
                .updateRecipies(name, urlImage, difficulty, author, recipeToEditDB, myRecipe)
        }
        _addEditUIState.update {
            AddEditUIState(isSuccess = true)
        }
    }

    fun insertRecipeDB(recipe: Recipies) {
        viewModelScope.launch(Dispatchers.IO) {
            db.recipiesDao().insertRecipies(recipe)
        }
        _addEditUIState.update {
            AddEditUIState(isSuccess = true)
        }
    }

    //------------------------ OTHERS
    fun isEditedOrNot(id: String) {
        if (id.length > 3) {
            _addEditUIState.update {
                AddEditUIState(isEdit = true)
            }
            getRecipe(id)
        }
    }

}

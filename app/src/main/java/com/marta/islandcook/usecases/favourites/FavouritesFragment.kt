package com.marta.islandcook.usecases.favourites

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.marta.islandcook.R
import com.marta.islandcook.databinding.FragmentFavouritesBinding
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.db.IslandCook_Database
import com.marta.islandcook.provider.db.entities.Recipies
import com.marta.islandcook.usecases.common.HomeListAdapter
import com.marta.islandcook.usecases.common.RecipesFromDBAdapter
import com.marta.islandcook.usecases.home.HomeFragmentDirections
import com.marta.islandcook.usecases.home.HomeUIState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class FavouritesFragment : Fragment() {
    private var _binding: FragmentFavouritesBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: FavouritesFragmentViewModel by viewModels()
    private val adapter: RecipesFromDBAdapter =
        RecipesFromDBAdapter(
            { navigateToRecipeDetail(it) },
            { likeDislike(it) },
            true)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.favouritesUIState.collect { favouritesUIState ->
                renderUIState(favouritesUIState)
            }
        }
        setUI()
        viewModel.getLikedRecipes()
    }
    //------------------------ UI
    private fun setUI() {
        setAdapter()
    }
    private fun setAdapter() {
        binding.rvFavourites.adapter = adapter
        binding.rvFavourites.layoutManager = GridLayoutManager(context,2)
    }
    private fun submitRecipesToAdapter(recipesList: List<Recipies> ) {
        adapter.submitList(recipesList)
    }
    private fun showHideEmptyListMsg(recipesList: List<Recipies> ){
        if(recipesList.isEmpty()){
            binding.tvNoItemsFoundFavorites.visibility = View.VISIBLE
        }else{
            binding.tvNoItemsFoundFavorites.visibility = View.GONE
        }
    }

    //------------------------ NAVIGATION

    private fun navigateToRecipeDetail(item: Recipies) {
        val action =
            FavouritesFragmentDirections.actionFavouritesFragmentToRecipeDetailFragment(item.recipeId)
        findNavController().navigate(action)
    }
    ///------------------------ UISTATE RELATED
    private suspend fun renderUIState(state: FavouritesUIState) = withContext(Dispatchers.Main) {
        if (state.isLoading) {
            state.recipeListDB?.let {
                showHideEmptyListMsg(it)
            }
        }
        if (state.isError) {
            showError()
        }
        if (state.isSuccess) {
            state.recipeListDB?.let {
                submitRecipesToAdapter(it)
                showHideEmptyListMsg(it)
            }
        }
    }

    private fun showError() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage("Recetas no encontradas.")
            .setPositiveButton("Vale") { dialog, which ->
            }
            .show()
    }

    //------------------------ DB REQUEST
    private fun likeDislike(item: Recipies) {
        viewModel.dislike(item)
    }
}
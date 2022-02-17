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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonDisposableHandle.parent
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FavouritesFragment : Fragment() {
    private var _binding: FragmentFavouritesBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: FavouritesFragmentViewModel by viewModels()
    private var recipesList: MutableList<Recipies> = mutableListOf()
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
            getLikedRecipes()
            viewModel.favouritesUIState.collect { favouritesUIState ->
                renderUIState(favouritesUIState)
            }
        }
        setUI()
        lifecycleScope.launch(Dispatchers.IO) {
            getLikedRecipes()
            withContext(Dispatchers.Main){
                submitRecipesToAdapter()
            }
        }
    }
    //------------------------ UI
    private fun setUI() {
        setAdapter()
    }
    private fun setAdapter() {
        binding.rvFavourites.adapter = adapter
        binding.rvFavourites.layoutManager = GridLayoutManager(context,2)
    }
    private fun submitRecipesToAdapter() {
        adapter.submitList(recipesList)
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
           //TODO
        }
        if (state.isError) {
            //TODO
            showError()
        }
        if (state.isSuccess) {
            submitRecipesToAdapter()
            //TODO ask how to acces DB from ViewModel with out context
        }
        //TODO Need to hide que msg for the empty cases if the list has items
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
    private suspend fun getLikedRecipes() {
       recipesList =
            IslandCook_Database.getInstance(requireContext()).recipiesDao().findAllRecipies().toMutableList()
    }

    private fun likeDislike(item: Recipies) {
        lifecycleScope.launch(Dispatchers.IO) {
            dislike(item)
            getLikedRecipes()
            withContext(Dispatchers.Main){
                submitRecipesToAdapter()
            }
        }
    }

    private suspend fun dislike(item: Recipies) {
        IslandCook_Database.getInstance(requireContext()).recipiesDao().deleteRecipie(item)
    }
}
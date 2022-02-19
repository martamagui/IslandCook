package com.marta.islandcook.usecases.recipes.recipeList

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.compose.ui.text.capitalize
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.marta.islandcook.databinding.FragmentRecipeListBinding
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.db.IslandCook_Database
import com.marta.islandcook.provider.db.entities.Recipies
import com.marta.islandcook.usecases.common.RecipesFromAPIAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecipeListFragment : Fragment() {
    private var _binding: FragmentRecipeListBinding? = null
    private val binding
        get() = _binding!!
    private val args: RecipeListFragmentArgs by navArgs()
    private val likedRecipes: MutableList<String> = mutableListOf()
    private val viewModel: RecipeListFragmentViewModel by viewModels()
    private val adapter: RecipesFromAPIAdapter =
        RecipesFromAPIAdapter({ navigateToRecipeDetail(it) },
            { navigateToRecipeDetail(it) },
            { isItLiked(it) })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUi()
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            getLikedRecipes()
            viewModel.recipeListUIState.collect { recipeListUIState ->
                renderUIState(recipeListUIState)
                Log.d("Statate", "$recipeListUIState")
            }
        }
        requestRecipeList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //------------------------ UI RELATED
    private suspend fun renderUIState(state: RecipeListUIState) = withContext(Dispatchers.Main) {
        if (state.isLoading) {
            binding.rvRecipesList.visibility = View.VISIBLE
            binding.shimmerRvListRecipes.visibility = View.VISIBLE
        }
        if (state.isError) {
            showError()
        }
        if (state.isSuccess) {
            submitRecipes(state.recipeList!!)
            binding.shimmerRvListRecipes.visibility = View.GONE
        }
    }

    private fun setUi() {
        binding.tvListTitle.text = if (args.filter == "") "All" else args.filter.capitalize()
        setAdapter()
    }

    private fun setAdapter() {
        binding.rvRecipesList.adapter = adapter
        binding.rvRecipesList.layoutManager = GridLayoutManager(context, 2)
    }
    private fun submitRecipes(list: List<RecipeResponse>) {
        adapter.submitList(list)
    }
    private fun showError() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage("Error de conexión.\nInténtalo de nuevo más tarde.")
            .setPositiveButton("Okay, Polisha") { dialog, which ->
                // Respond to positive button press
            }
            .show()
    }
    private fun hideChips(){
        with(binding){
            chipEasyList.visibility = View.GONE
            chipMediumList.visibility = View.GONE
            chipHardList.visibility = View.GONE
        }
    }
    //------------------------ API REQUEST
    private fun requestRecipeList() {
        val filter = args.filter
        if (filter != "easy" && filter != "medium" && filter != "hard") {
            viewModel.getRecipesFromAPIbyTag(filter)
        } else {
            viewModel.getRecipesFromAPIbyDifficulty(filter)
            hideChips()
        }
    }
    private fun requestRecipeListDoubleFilter(difficultity: String) {

    }
    //------------------------ DB
    private suspend fun getLikedRecipes() {
        likedRecipes.clear()
        val savedRecipes =
            IslandCook_Database.getInstance(requireContext()).recipiesDao().findAllRecipies()
        savedRecipes.forEach { likedRecipes.add(it.recipeId) }
    }

    private fun isItLiked(item: RecipeResponse): Boolean {
        return likedRecipes.contains(item.id)
    }

    private fun likeDislike(item: RecipeResponse) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            if (likedRecipes.contains(item.id)) {
                dislike(item)
                likedRecipes.remove(item.id)
            } else {
                saveRecipe(item)
                likedRecipes.add(item.id)
            }
        }
    }

    private suspend fun dislike(item: RecipeResponse) {
        IslandCook_Database.getInstance(requireContext()).recipiesDao().deleteRecipieById(item.id)
    }

    private suspend fun saveRecipe(item: RecipeResponse) {
        IslandCook_Database.getInstance(requireContext()).recipiesDao().insertRecipies(
            Recipies(
                item.id,
                item.name,
                item.pictureUrl,
                item.difficulty,
                item.author,
                false
            )
        )
        likedRecipes.add(item.id)
    }

    //------------------------ NAVIGATION
    private fun navigateToRecipeDetail(it: RecipeResponse) {
        val action =
            RecipeListFragmentDirections.actionRecipeListFragmentToRecipeDetailFragment(it.id)
        findNavController().navigate(action)
    }
}
package com.marta.islandcook.usecases.recipes.recipeList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
    private val likedRecipes: MutableList<String> = mutableListOf()
    private val viewModel: RecipeListFragmentViewModel by viewModels()
    private val adapter: RecipesFromAPIAdapter = RecipesFromAPIAdapter({navigateToRecipeDetail(it)},{navigateToRecipeDetail(it)},{isItLiked(it)})

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            getLikedRecipes()
            viewModel.recipeListUIState.collect { recipeListUIState ->
                renderUIState(recipeListUIState)
            }
        }
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
    private fun showError() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage("Error de conexión.\nInténtalo de nuevo más tarde.")
            .setPositiveButton("Okay, Polisha") { dialog, which ->
                // Respond to positive button press
            }
            .show()
    }
    private fun submitRecipes(list: List<RecipeResponse>){
        adapter.submitList(list)
    }
    //------------------------ API REQUEST

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

    private fun likeDislike(item: RecipeResponse){
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            if(likedRecipes.contains(item.id)){
                dislike(item)
                likedRecipes.remove(item.id)
            }else{
                saveRecipe(item)
                likedRecipes.add(item.id)
            }
        }
    }
    private suspend fun dislike(item: RecipeResponse){
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
        val action = RecipeListFragmentDirections.actionRecipeListFragmentToRecipeDetailFragment(it.id)
        findNavController().navigate(action)
    }
}
package com.marta.islandcook.usecases.recipes.RecipeDetail

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.marta.islandcook.R
import com.marta.islandcook.databinding.FragmentRecipeDetailBinding
import com.marta.islandcook.model.response.Ingredient
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.db.IslandCook_Database
import com.marta.islandcook.provider.db.entities.Recipies
import com.marta.islandcook.utils.imageUrl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RecipeDetailFragment : Fragment() {
    private var recipe: RecipeResponse? = null
    private val args: RecipeDetailFragmentArgs by navArgs()
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding
        get() = _binding!!
    private val viewModel: RecipeDetailViewModel by viewModels()
    private val likedRecipes: MutableList<String> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            getLikedRecipes()
            viewModel.detailUIState.collect { detailUIState ->
                renderUIState(detailUIState)
            }
            changeIconLike(isItLiked())
        }
        viewModel.getRecipeFromAPI(args.recipeId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //------------------------ UI

    private suspend fun renderUIState(state: RecipeDetailUIState) {
        if (state.isLoading) {
            binding.svDetail.visibility = View.GONE
            binding.shimmerDetail.visibility = View.VISIBLE
        }
        if (state.isError) {
            showError()
            delay(500)
            backToPreviousFragment()
        }
        if (state.isSuccess) {
            binding.svDetail.visibility = View.VISIBLE
            binding.shimmerDetail.visibility = View.GONE
            populateUI(state.recipe!!)
        }
        if (state.isDeleted) {
            backToPreviousFragment()
        }
    }


    private suspend fun showError() = withContext(Dispatchers.Main) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage("Error de conexión.\nInténtalo de nuevo más tarde")
            .setPositiveButton("Okay, Polisha") { dialog, which -> }
            .show()
    }

    private suspend fun populateUI(recipeResponse: RecipeResponse) = withContext(Dispatchers.Main) {
        recipeResponse?.let {
            binding.tvNameRecipeDetail.text = it.name
            binding.ivImgRecipe.imageUrl(it.pictureUrl)
            binding.tvAuthorDetail.text = it.author
            binding.tvIngredientsDetail.text = getIngredientsString(recipeResponse.ingredients)
            binding.tvStepsDetail.text = getStepsString(recipeResponse.steps)
            recipeResponse.tags.forEach {
                addChip(it)
            }
            setBtns(recipeResponse)
        }
    }

    private fun addChip(chipText: String) {
        val chip = Chip(requireContext())
        chip.text = chipText
        chip.isClickable = false
        chip.chipBackgroundColor =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tertiary90))
        chip.chipStrokeColor =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.tertiary90))
        binding.chipTagsDetail.addView(chip as View)
    }

    private fun getIngredientsString(ingredients: List<Ingredient>): String {
        var stringIngredients = ""
        ingredients.forEach {
            stringIngredients += "- ${it.name}: ${it.amount} \n\n"
        }
        return stringIngredients
    }

    private fun getStepsString(steps: List<String>): String {
        var stringSteps = ""
        steps.forEach {
            stringSteps += "- ${it} \n\n"
        }
        return stringSteps
    }

    private fun setBtns(recipeResponse: RecipeResponse) {
        binding.btnDeleteDetail.setOnClickListener {
            viewModel.deleteFromAPI(args.recipeId)
        }
        binding.ibUpdateDetail.setOnClickListener { navigateToEdit() }
        binding.ibLikeDetail.setOnClickListener {
            likeDislike(recipeResponse)
        }
    }

    private suspend fun changeIconLike(liked: Boolean) = withContext(Dispatchers.Main) {
        if (!liked) {
            binding.ibLikeDetail.setImageResource(R.drawable.ic_baseline_favorite_border_35)
        } else {
            binding.ibLikeDetail.setImageResource(R.drawable.ic_baseline_favorite_35)
        }
    }

    //------------------------ DB
    private suspend fun getLikedRecipes() {
        likedRecipes.clear()
        val savedRecipes =
            IslandCook_Database.getInstance(requireContext()).recipiesDao().findAllRecipies()
        savedRecipes.forEach { likedRecipes.add(it.recipeId) }
    }

    private fun isItLiked(): Boolean {
        return likedRecipes.contains(args.recipeId)
    }

    private fun likeDislike(item: RecipeResponse) {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            if (isItLiked()) {
                viewModel.dislike(item)
                likedRecipes.remove(args.recipeId)
                changeIconLike(false)
            } else {
                viewModel.saveRecipe(item)
                likedRecipes.add(args.recipeId)
                changeIconLike(true)
            }
        }
    }
    //------------------------ NAVIGATION
    private fun navigateToEdit() {
        val action =
            RecipeDetailFragmentDirections.actionRecipeDetailFragmentToAddEditRecipeFragment(
                args.recipeId
            )
        findNavController().navigate(action)
    }

    private fun backToPreviousFragment() {
        parentFragmentManager.popBackStack()
    }
}
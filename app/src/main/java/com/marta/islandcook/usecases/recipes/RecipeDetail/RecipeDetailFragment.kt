package com.marta.islandcook.usecases.recipes.RecipeDetail

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.marta.islandcook.R
import com.marta.islandcook.databinding.FragmentRecipeDetailBinding
import com.marta.islandcook.model.response.RecipeResponse
import com.marta.islandcook.provider.api.NetworkManagerRecipesAPI
import com.marta.islandcook.provider.db.IslandCook_Database
import com.marta.islandcook.provider.db.entities.Recipies
import com.marta.islandcook.utils.imageUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecipeDetailFragment : Fragment() {
    private var recipe: RecipeResponse? = null
    private val args: RecipeDetailFragmentArgs by navArgs()
    private var _binding: FragmentRecipeDetailBinding? = null
    private val binding
        get() = _binding!!
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
        binding.svDetail.visibility = View.GONE
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            getLikedRecipes()
            requestData(args.recipeId)
            changeIconLike(isItLiked())
            withContext(Dispatchers.Main) {
                populateUI(recipe!!)
            }
            binding.ibUpdateDetail.setOnClickListener{
                navigateToEdit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //------------------------ UI
    private suspend fun showError() = withContext(Dispatchers.Main) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Error")
            .setMessage("Error de conexión.\nInténtalo de nuevo más tarde")
            .setPositiveButton("Okay, Polisha") { dialog, which ->
                // Respond to positive button press
            }
            .show()
    }

    private fun populateUI(recipeResponse: RecipeResponse) {
        setBtnFunctionalities()
        var stringIngredients = ""
        var stringSteps = ""
        recipeResponse?.let {
            binding.tvNameRecipeDetail.text = it.name
            binding.ivImgRecipe.imageUrl(it.pictureUrl)
            binding.tvAuthorDetail.text = it.author
            recipeResponse.ingredients.forEach {
                stringIngredients += "- ${it.name}: ${it.amount} \n\n"
            }
            recipeResponse.steps.forEach {
                stringSteps += "- ${it} \n\n"
            }
            binding.tvIngredientsDetail.text = stringIngredients
            binding.tvStepsDetail.text = stringSteps
            binding.ibLikeDetail.setOnClickListener {
                likeDislike(recipeResponse)
            }
            recipeResponse.tags.forEach {
                addChip(it)
            }
        }
        binding.svDetail.visibility = View.VISIBLE
        binding.shimmerDetail.visibility = View.GONE
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
    private fun setBtnFunctionalities(){
        with(binding){
            btnDeleteDetail.setOnClickListener{
                deleteRecipe()
            }
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
                dislike()
                likedRecipes.remove(args.recipeId)
                changeIconLike(false)
            } else {
                saveRecipe(item)
                likedRecipes.add(args.recipeId)
                changeIconLike(true)
            }
        }
    }

    private suspend fun dislike() {
        IslandCook_Database.getInstance(requireContext()).recipiesDao()
            .deleteRecipieById(args.recipeId)
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

    //------------------------ API
    private suspend fun requestData(recipeId: String) {
        delay(500)
        try {
            recipe = NetworkManagerRecipesAPI.service.getRecipeById(recipeId)
        } catch (error: Exception) {
            showError()
        }
    }
    private fun deleteRecipe() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO){
            try {
                NetworkManagerRecipesAPI.service.deleteRecipe(args.recipeId)
            } catch (error: Exception) {
                showError()
            }
        }
        parentFragmentManager.popBackStack()
    }
    //------------------------ NAVIGATION
    private fun navigateToEdit(){
        val action = RecipeDetailFragmentDirections.actionRecipeDetailFragmentToAddEditRecipeFragment(
                args.recipeId
            )
        findNavController().navigate(action)
    }
}